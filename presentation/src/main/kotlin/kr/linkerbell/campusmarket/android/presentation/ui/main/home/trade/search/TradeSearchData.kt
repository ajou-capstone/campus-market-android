package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search

import androidx.compose.runtime.Immutable

@Immutable
data class TradeSearchData(
    val searchHistory: List<String>,
    val previousQuery: String
)
