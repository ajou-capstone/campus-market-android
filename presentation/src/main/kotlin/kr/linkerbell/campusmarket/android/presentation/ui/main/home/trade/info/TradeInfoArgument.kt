package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kotlin.coroutines.CoroutineContext

@Immutable
data class TradeInfoArgument(
    val state: TradeInfoState,
    val event: EventFlow<TradeInfoEvent>,
    val intent: (TradeInfoIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TradeInfoState {
    data object Init : TradeInfoState
    data object Loading : TradeInfoState
}


sealed interface TradeInfoEvent {
    data class NavigateToChatRoom(val id: Long) : TradeInfoEvent
}

sealed interface TradeInfoIntent {
    data object LikeButtonClicked : TradeInfoIntent
    data object DeleteThisPost : TradeInfoIntent
    data object OnTradeStart : TradeInfoIntent
    data object RefreshNewTrades : TradeInfoIntent
}
