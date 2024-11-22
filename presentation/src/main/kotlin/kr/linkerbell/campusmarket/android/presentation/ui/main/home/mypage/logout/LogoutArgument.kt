package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class LogoutArgument(
    val state: LogoutState,
    val event: EventFlow<LogoutEvent>,
    val intent: (LogoutIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface LogoutState {
    data object Init : LogoutState
    data object Loading : LogoutState
}


sealed interface LogoutEvent

sealed interface LogoutIntent{
    data object LogOut: LogoutIntent
}
