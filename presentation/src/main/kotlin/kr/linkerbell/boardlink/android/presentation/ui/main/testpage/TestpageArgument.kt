package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow

@Immutable
data class TestPageArgument(
    val state: TestPageState,
    val event: EventFlow<TestPageEvent>,
    val intent: (TestPageIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TestPageState {
    data object Init : TestPageState
    data object Loading : TestPageState
}

sealed interface TestPageEvent

sealed interface TestPageIntent
