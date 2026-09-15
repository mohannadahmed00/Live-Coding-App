package com.giraffe.livecodigapp.interview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun interface SearchRepository {
    suspend fun search(query: String): List<String>
}

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Loading : SearchUiState
    data class Success(val items: List<String>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}

/**
 * LIVE-CODING QUESTION (Task A in the supplied mock-interviewer PDF):
 * Build a ViewModel that debounces input, ignores blanks and duplicate consecutive queries,
 * cancels an old search when a new query arrives, and exposes immutable UI state.
 *
 * ANSWER EXPLANATION:
 * - MutableStateFlow is private so only the ViewModel can mutate it.
 * - debounce waits for typing to pause; distinctUntilChanged avoids a duplicate request.
 * - flatMapLatest cancels the previous search Flow when a new query arrives, preventing stale UI.
 * - stateIn converts the pipeline to a hot StateFlow suitable for Compose screen state.
 * - CancellationException is rethrown because cancellation is control flow, not a user error.
 *
 * ViewModel survives configuration changes, but not process death. Use SavedStateHandle for a small
 * restorable query and persistent storage/repositories for durable data when the requirement needs it.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel @JvmOverloads constructor(
    private val repository: SearchRepository = DemoSearchRepository(),
    debounceMillis: Long = 300L,
) : ViewModel() {
    private data class SearchRequest(val query: String, val attempt: Int = 0)

    private val request = MutableStateFlow(SearchRequest(""))
    private val mutableQuery = MutableStateFlow("")
    val query: StateFlow<String> = mutableQuery.asStateFlow()

    val uiState: StateFlow<SearchUiState> = request
        .debounce(debounceMillis)
        .map { searchRequest -> searchRequest.copy(query = searchRequest.query.trim()) }
        .distinctUntilChanged()
        .flatMapLatest { searchRequest -> searchState(searchRequest.query) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = SearchUiState.Idle,
        )

    fun onQueryChanged(value: String) {
        mutableQuery.value = value
        request.value = SearchRequest(query = value)
    }

    fun onRetry() {
        request.value = request.value.copy(attempt = request.value.attempt + 1)
    }

    private fun searchState(query: String): Flow<SearchUiState> {
        if (query.isBlank()) return flowOf(SearchUiState.Idle)

        return flow {
            emit(SearchUiState.Loading)
            emit(SearchUiState.Success(repository.search(query)))
        }.catch { error ->
            if (error is CancellationException) throw error
            emit(SearchUiState.Error(error.message ?: "Search failed"))
        }
    }
}

/** A local fake keeps the project runnable without API keys, network, DI, or an emulator connection. */
private class DemoSearchRepository : SearchRepository {
    private val candidates = listOf(
        "Kotlin coroutines",
        "StateFlow and SharedFlow",
        "Jetpack Compose state",
        "ViewModel lifecycle",
        "MVVM and UDF",
        "Room offline-first",
        "Retrofit error handling",
        "Dagger dependency injection",
        "Unit testing with runTest",
        "Data structures and algorithms",
    )

    override suspend fun search(query: String): List<String> {
        delay(700) // Makes cancellation/loading visible during practice.
        if (query.equals("error", ignoreCase = true)) error("Practice error: tap Retry or edit the query")
        return candidates.filter { candidate -> candidate.contains(query, ignoreCase = true) }
    }
}
