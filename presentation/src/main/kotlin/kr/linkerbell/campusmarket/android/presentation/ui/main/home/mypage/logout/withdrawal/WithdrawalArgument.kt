package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class WithdrawalArgument(
    val state: WithdrawalState,
    val event: EventFlow<WithdrawalEvent>,
    val intent: (WithdrawalIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface WithdrawalState {
    data object Init : WithdrawalState
    data object Loading : WithdrawalState
}

sealed interface WithdrawalEvent {
    data object WithdrawalSuccess : WithdrawalEvent
}

sealed interface WithdrawalIntent {
    data object Withdrawal : WithdrawalIntent
}
