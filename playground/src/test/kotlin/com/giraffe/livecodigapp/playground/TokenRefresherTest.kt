package com.giraffe.livecodigapp.playground

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TokenRefresherTest {
    @Test
    fun `concurrent callers share one refresh`() = runTest {
        var storedToken: String? = "expired"
        var refreshCount = 0
        val store = object : TokenStore {
            override fun currentAccessToken(): String? = storedToken
            override suspend fun saveAccessToken(token: String) {
                storedToken = token
            }
        }
        val refresher = TokenRefresher(store) {
            refreshCount += 1
            delay(100) // Keeps the lock held so the other callers genuinely wait for the same refresh.
            "fresh"
        }

        val results = List(5) {
            async { refresher.refreshIfNeeded(tokenUsedByFailedRequest = "expired") }
        }.awaitAll()

        assertEquals(List(5) { "fresh" }, results)
        assertEquals(1, refreshCount)
    }
}
