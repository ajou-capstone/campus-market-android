package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import androidx.compose.runtime.Immutable
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData.UserCalender

@Immutable
data class testpageArgument(
    val state: testpageState,
    val event: EventFlow<testpageEvent>,
    val intent: (testpageIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface testpageState {
    data object Init : testpageState
    data class Loaded(val userCalender: UserCalender) : testpageState
}


sealed interface testpageEvent

sealed interface testpageIntent
