package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade

@Immutable
data class TradeSearchResultData(
    val tradeList: LazyPagingItems<Trade>
)

@Immutable
data class TradeSearchQuery(
    var name: String = "",
    var category: String = "",
    var minPrice: Int = 0,
    var maxPrice: Int = 0,
    var sorted: String = "createdDate,desc"
)
