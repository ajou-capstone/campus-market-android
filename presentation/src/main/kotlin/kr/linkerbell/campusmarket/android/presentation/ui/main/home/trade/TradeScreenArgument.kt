package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class TradeScreenArgument(
    val state: TradeScreenState,
    val event: EventFlow<TradeScreenEvent>,
    val intent: (TradeScreenIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TradeScreenState {
    data object Init : TradeScreenState
}

sealed interface TradeScreenEvent

sealed interface TradeScreenIntent{
    data object RefreshNewTrades : TradeScreenIntent
}
