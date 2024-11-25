package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class RecentTradeArgument(
    val state: RecentTradeState,
    val event: EventFlow<RecentTradeEvent>,
    val intent: (RecentTradeIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RecentTradeState {
    data object Init : RecentTradeState
    data object Loading : RecentTradeState
}

sealed interface RecentTradeEvent

sealed interface RecentTradeIntent
