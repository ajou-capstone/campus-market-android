package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.UserProfileConstant

object RecentReviewConstant {
    const val ROUTE = "${UserProfileConstant.ROUTE}/recentReview"

    const val ROUTE_ARGUMENT_USER_ID = "userId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}"
}
