package kr.linkerbell.campusmarket.android.presentation.ui.main.home.landingpage

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade

@Immutable
data class LandingPageData(
    val tradeList: LazyPagingItems<Trade>
)
