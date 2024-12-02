package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile

object UserProfileConstant {
    const val ROUTE: String = "userProfile"

    const val ROUTE_ARGUMENT_USER_ID = "userId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}"
}
