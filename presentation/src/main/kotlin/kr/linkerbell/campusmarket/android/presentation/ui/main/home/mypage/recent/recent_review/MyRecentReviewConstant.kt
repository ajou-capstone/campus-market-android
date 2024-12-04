package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.MyPageConstant

object MyRecentReviewConstant {
    const val ROUTE = "${MyPageConstant.ROUTE}/myRecentReview"

    const val ROUTE_ARGUMENT_USER_ID = "userId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}"
}
