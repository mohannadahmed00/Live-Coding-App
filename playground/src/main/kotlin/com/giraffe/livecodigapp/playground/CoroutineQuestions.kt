package com.giraffe.livecodigapp.playground

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class Dashboard(val userName: String, val unreadCount: Int)

interface DashboardApi {
    suspend fun loadUserName(): String
    suspend fun loadUnreadCount(): Int
}

/**
 * QUESTION: launch vs async, and what is structured concurrency?
 *
 * ANSWER: launch returns Job and is for work with no result. async returns Deferred<T>, and await()
 * retrieves its result. coroutineScope owns both children: it waits for them, and cancellation/failure
 * propagates through the scope. `suspend` itself does NOT switch to a background thread.
 *
 * The two independent calls below run concurrently, so total time is close to the slower call rather
 * than their sum. Do not use GlobalScope for screen work; Android screen work belongs to viewModelScope
 * or another lifecycle-owned scope.
 */
suspend fun loadDashboard(api: DashboardApi): Dashboard = coroutineScope {
    val userName = async { api.loadUserName() }
    val unreadCount = async { api.loadUnreadCount() }
    Dashboard(userName.await(), unreadCount.await())
}

class FakeDashboardApi : DashboardApi {
    override suspend fun loadUserName(): String {
        delay(50)
        return "Mohannad"
    }

    override suspend fun loadUnreadCount(): Int {
        delay(80)
        return 3
    }
}

interface TokenStore {
    fun currentAccessToken(): String?
    suspend fun saveAccessToken(token: String)
}

fun interface AuthApi {
    suspend fun refreshToken(): String
}

/**
 * QUESTION (harder task from the mock PDF): Several requests receive 401 together. Ensure only one
 * refresh runs, while every caller receives the new token.
 *
 * ANSWER: Mutex serializes refresh attempts without blocking a thread. The second token check MUST
 * happen inside the lock: a caller may have waited while another caller refreshed and saved a token.
 * In production, also prevent the refresh request itself from entering an infinite 401 retry loop.
 */
class TokenRefresher(
    private val tokenStore: TokenStore,
    private val authApi: AuthApi,
) {
    private val mutex = Mutex()

    suspend fun refreshIfNeeded(tokenUsedByFailedRequest: String?): String = mutex.withLock {
        val currentToken = tokenStore.currentAccessToken()
        if (currentToken != null && currentToken != tokenUsedByFailedRequest) {
            return@withLock currentToken
        }

        val refreshedToken = authApi.refreshToken()
        tokenStore.saveAccessToken(refreshedToken)
        refreshedToken
    }
}
