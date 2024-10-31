package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradepost

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class TradePostArgument(
    val state: TradePostState,
    val event: EventFlow<TradePostEvent>,
    val intent: (TradePostIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TradePostState {
    data object Init : TradePostState
    data object Loading : TradePostState
}


sealed interface TradePostEvent

sealed interface TradePostIntent
