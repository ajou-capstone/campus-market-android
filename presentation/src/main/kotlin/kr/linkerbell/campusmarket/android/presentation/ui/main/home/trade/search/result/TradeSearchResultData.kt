package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade

@Immutable
data class TradeSearchResultData(
    val tradeList: LazyPagingItems<Trade>,
    val currentQuery: TradeSearchQuery,
    val categoryList: List<String>
)

@Immutable
data class TradeSearchQuery(
    val name: String = "",
    val category: String = "",
    val minPrice: Int = 0,
    val maxPrice: Int = Int.MAX_VALUE,
    val sorted: String = "createdDate,desc"
)
