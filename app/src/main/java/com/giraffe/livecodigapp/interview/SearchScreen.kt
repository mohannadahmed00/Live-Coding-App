package com.giraffe.livecodigapp.interview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.giraffe.livecodigapp.ui.theme.LiveCodigAppTheme

/**
 * LIVE-CODING QUESTION (Task C in the supplied mock-interviewer PDF):
 * Collect StateFlow lifecycle-aware and build a stateless Compose screen using UDF.
 *
 * ANSWER: The route owns Android/ViewModel integration. SearchScreen receives plain immutable state
 * and callbacks: state goes down, events go up. It can therefore be previewed and tested without a
 * ViewModel. collectAsStateWithLifecycle stops collection when the UI lifecycle is inactive.
 */
@Composable
fun SearchRoute(
    contentPadding: PaddingValues,
    viewModel: SearchViewModel = viewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        query = query,
        state = state,
        contentPadding = contentPadding,
        onQueryChanged = viewModel::onQueryChanged,
        onRetry = viewModel::onRetry,
        onItemClick = { selected -> println("Selected interview topic: $selected") },
    )
}

@Composable
fun SearchScreen(
    query: String,
    state: SearchUiState,
    contentPadding: PaddingValues,
    onQueryChanged: (String) -> Unit,
    onRetry: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Android Interview Playground", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Type a topic. Try “flow”, “viewmodel”, or “error”. The code demonstrates debounce, " +
                "latest-work cancellation, immutable state, and unidirectional data flow.",
            style = MaterialTheme.typography.bodyMedium,
        )
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search interview topics") },
            singleLine = true,
        )

        when (state) {
            SearchUiState.Idle -> Text("Waiting for a question…")
            SearchUiState.Loading -> {
                Spacer(Modifier.height(8.dp))
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            is SearchUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = onRetry) { Text("Retry") }
            }
            is SearchUiState.Success -> {
                if (state.items.isEmpty()) {
                    Text("No matching topics")
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(state.items, key = { item -> item }) { item ->
                            Text(
                                text = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onItemClick(item) }
                                    .padding(vertical = 14.dp),
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    LiveCodigAppTheme {
        SearchScreen(
            query = "flow",
            state = SearchUiState.Success(listOf("StateFlow and SharedFlow")),
            contentPadding = PaddingValues(),
            onQueryChanged = {},
            onRetry = {},
            onItemClick = {},
        )
    }
}
