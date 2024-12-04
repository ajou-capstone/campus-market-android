package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade

@Immutable
data class MyRecentTradeData(
    val recentTrades: LazyPagingItems<RecentTrade>,
    val recentBuyTrades: LazyPagingItems<RecentTrade>,
    val recentSellTrades: LazyPagingItems<RecentTrade>
)
