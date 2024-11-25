package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade

@Immutable
data class RecentTradeData(
    val recentTrades: LazyPagingItems<RecentTrade>
)
