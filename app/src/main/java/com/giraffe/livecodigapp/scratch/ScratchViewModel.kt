package com.giraffe.livecodigapp.scratch

import androidx.lifecycle.ViewModel

/**
 * CLEAN START for a ViewModel/StateFlow question.
 *
 * A common interview shape is:
 * 1. Define immutable UiState.
 * 2. Keep MutableStateFlow private and expose StateFlow.
 * 3. Receive UI events through functions.
 * 4. Launch asynchronous work in viewModelScope.
 * 5. Emit loading, success/empty, and error states; do not swallow cancellation.
 *
 * SearchViewModel.kt contains a complete, tested answer when you want a reference.
 */
class ScratchViewModel : ViewModel() {
    // Write the interview solution here.
}
