package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade

@Immutable
data class TradeData(
    val tradeList: LazyPagingItems<Trade>
)
