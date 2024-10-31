package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.TradeSearchConstant

object TradeSearchResultConstant {

    const val ROUTE: String = "${TradeSearchConstant.ROUTE}/result"
    const val ROUTE_STRUCTURE =
        "${ROUTE}?name={name}&category={category}&minPrice={minPrice}&maxPrice={maxPrice}&sorted={sorted}"
}
