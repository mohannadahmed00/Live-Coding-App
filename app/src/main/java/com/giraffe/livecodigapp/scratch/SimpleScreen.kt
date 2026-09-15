package com.giraffe.livecodigapp.scratch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giraffe.livecodigapp.ui.theme.LiveCodigAppTheme

/**
 * Empty starting point for Jetpack Compose interview tasks.
 */
@Composable
fun SimpleScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        // Build the interview UI here.
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleScreenPreview() {
    LiveCodigAppTheme {
        SimpleScreen()
    }
}
