package com.giraffe.livecodigapp.interview

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `new query cancels stale search result`() = runTest(mainDispatcherRule.dispatcher) {
        val repository = SearchRepository { query ->
            delay(if (query == "first") 1_000 else 10)
            listOf(query)
        }
        val viewModel = SearchViewModel(repository)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.onQueryChanged("first")
        advanceTimeBy(301)
        runCurrent()
        assertEquals(SearchUiState.Loading, viewModel.uiState.value)

        viewModel.onQueryChanged("second")
        advanceTimeBy(301)
        advanceUntilIdle()

        assertEquals(SearchUiState.Success(listOf("second")), viewModel.uiState.value)
    }
}
