package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.result

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class TradeSearchResultArgument(
    val state: TradeSearchResultState,
    val event: EventFlow<TradeSearchResultEvent>,
    val intent: (TradeSearchResultIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TradeSearchResultState {
    data object Init : TradeSearchResultState
    data object Loading : TradeSearchResultState
}

sealed interface TradeSearchResultEvent

sealed interface TradeSearchResultIntent {
    data class ApplyNewQuery(val newQuery: TradeSearchQuery) : TradeSearchResultIntent
    data object RefreshNewTrades : TradeSearchResultIntent
}

