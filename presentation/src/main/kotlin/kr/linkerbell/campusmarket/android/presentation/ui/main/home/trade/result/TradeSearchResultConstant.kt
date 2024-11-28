package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.result

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.TradeSearchConstant

object TradeSearchResultConstant {

    const val ROUTE: String = "${TradeSearchConstant.ROUTE}/result"

    const val ROUTE_ARGUMENT_NAME = "name"
    const val ROUTE_ARGUMENT_CATEGORY = "category"
    const val ROUTE_ARGUMENT_MINPRICE = "minPrice"
    const val ROUTE_ARGUMENT_MAXPRICE = "maxPrice"
    const val ROUTE_ARGUMENT_SORTED = "sort"

    const val ROUTE_STRUCTURE = "$ROUTE?" +
            "$ROUTE_ARGUMENT_NAME={$ROUTE_ARGUMENT_NAME}" +
            "&$ROUTE_ARGUMENT_CATEGORY={$ROUTE_ARGUMENT_CATEGORY}" +
            "&$ROUTE_ARGUMENT_MINPRICE={$ROUTE_ARGUMENT_MINPRICE}" +
            "&$ROUTE_ARGUMENT_MAXPRICE={$ROUTE_ARGUMENT_MAXPRICE}" +
            "&$ROUTE_ARGUMENT_SORTED={$ROUTE_ARGUMENT_SORTED}"
}
