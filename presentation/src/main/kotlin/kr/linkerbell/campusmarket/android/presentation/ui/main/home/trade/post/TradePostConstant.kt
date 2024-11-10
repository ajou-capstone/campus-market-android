package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.TradeConstant

object TradePostConstant {
    const val ROUTE: String = "${TradeConstant.ROUTE}/post"
    const val ROUTE_ARGUMENT_ITEM_ID = "itemId"

    const val ROUTE_STRUCTURE = "${ROUTE}?$ROUTE_ARGUMENT_ITEM_ID={$ROUTE_ARGUMENT_ITEM_ID}"
}
