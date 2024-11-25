package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.UserProfileConstant

object RecentTradeConstant {
    const val ROUTE = "${UserProfileConstant.ROUTE}/recentTrade"

    const val ROUTE_ARGUMENT_USER_ID = "userId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}"
}
