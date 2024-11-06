package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.TradeConstant

object TradeSearchConstant {
    const val ROUTE = "${TradeConstant.ROUTE}/search"

    const val ROUTE_ARGUMENT_NAME = "name"

    const val ROUTE_STRUCTURE = "$ROUTE?$ROUTE_ARGUMENT_NAME={$ROUTE_ARGUMENT_NAME}"
}
