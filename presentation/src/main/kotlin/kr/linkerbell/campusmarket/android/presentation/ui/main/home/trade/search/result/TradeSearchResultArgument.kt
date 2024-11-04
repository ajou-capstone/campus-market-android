package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kotlin.coroutines.CoroutineContext

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
    data class ApplyCategoryFilter(val newCategoryOption: String) : TradeSearchResultIntent
    data class ApplyMinPriceFilter(val newMinPriceFilter: Int) : TradeSearchResultIntent
    data class ApplyMaxPriceFilter(val newMaxPriceFilter: Int) : TradeSearchResultIntent
    data class ApplySortingFilter(val newSortingFilter: String) : TradeSearchResultIntent

}

