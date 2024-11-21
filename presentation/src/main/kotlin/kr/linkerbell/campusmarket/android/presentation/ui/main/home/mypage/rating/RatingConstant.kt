package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.TradeConstant

object RatingConstant {
    const val ROUTE: String = "${TradeConstant.ROUTE}/rating"
    const val ROUTE_ARGUMENT_USER_ID = "userId"
    const val ROUTE_ARGUMENT_ITEM_ID = "itemId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}" +
            "&$ROUTE_ARGUMENT_ITEM_ID={$ROUTE_ARGUMENT_ITEM_ID}"
}

