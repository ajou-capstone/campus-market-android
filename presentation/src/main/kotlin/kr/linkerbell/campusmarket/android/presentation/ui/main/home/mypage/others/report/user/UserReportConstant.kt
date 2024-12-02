package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.user

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.MyPageConstant

object UserReportConstant {
    const val ROUTE = "${MyPageConstant.ROUTE}/report/user"

    const val ROUTE_ARGUMENT_USER_ID = "userId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?${ROUTE_ARGUMENT_USER_ID}={${ROUTE_ARGUMENT_USER_ID}}"
}
