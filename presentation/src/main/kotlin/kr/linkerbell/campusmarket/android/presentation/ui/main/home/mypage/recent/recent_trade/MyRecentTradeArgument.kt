package kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class MyRecentTradeArgument(
    val state: MyRecentTradeState,
    val event: EventFlow<MyRecentTradeEvent>,
    val intent: (MyRecentTradeIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface MyRecentTradeState {
    data object Init : MyRecentTradeState
    data object Loading : MyRecentTradeState
}

sealed interface MyRecentTradeEvent {
    data object RateSuccess : MyRecentTradeEvent
    data object RateFail : MyRecentTradeEvent
}

sealed interface MyRecentTradeIntent {
    data object RefreshAllTradeList : MyRecentTradeIntent
    data object RefreshSellTradeList : MyRecentTradeIntent
    data object RefreshBuyTradeList : MyRecentTradeIntent

    data object RefreshTradeList : MyRecentTradeIntent

    data class RateUser(
        val targetUserId: Long,
        val itemId: Long,
        val description: String,
        val rating: Int
    ) : MyRecentTradeIntent
}
