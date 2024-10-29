package kr.linkerbell.campusmarket.android.presentation.ui.main.home.tradesearchpage

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class TradeSearchArgument(
    val state: TradeSearchState,
    val event: EventFlow<TradeSearchEvent>,
    val intent: (TradeSearchIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TradeSearchState {
    data object Init : TradeSearchState
    data object Loading : TradeSearchState
}


sealed interface TradeSearchEvent

sealed interface TradeSearchIntent
