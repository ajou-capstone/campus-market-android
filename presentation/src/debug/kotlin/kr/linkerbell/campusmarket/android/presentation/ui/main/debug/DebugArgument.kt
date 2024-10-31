package kr.linkerbell.campusmarket.android.presentation.ui.main.debug

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class DebugArgument(
    val state: DebugState,
    val event: EventFlow<DebugEvent>,
    val intent: (DebugIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface DebugState {
    data object Init : DebugState
}

sealed interface DebugEvent

sealed interface DebugIntent
