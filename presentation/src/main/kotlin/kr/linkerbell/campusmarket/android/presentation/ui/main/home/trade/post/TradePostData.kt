package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents

@Immutable
data class TradePostData(
    val categoryList: List<String>,
    val originalTradeContents: TradeContents
)
