package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent_trade

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.MyPageConstant

object MyRecentTradeConstant {
    const val ROUTE = "${MyPageConstant.ROUTE}/myRecentTrade"

    const val ROUTE_ARGUMENT_USER_ID = "userId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}"
}
