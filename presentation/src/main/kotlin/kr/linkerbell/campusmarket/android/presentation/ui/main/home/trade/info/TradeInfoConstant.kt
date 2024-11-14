package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.TradeConstant

object TradeInfoConstant {

    const val ROUTE = "${TradeConstant.ROUTE}/info"
    const val ROUTE_ARGUMENT_ITEM_ID = "itemId"

    const val ROUTE_STRUCTURE = "$ROUTE?$ROUTE_ARGUMENT_ITEM_ID={$ROUTE_ARGUMENT_ITEM_ID}"
}
