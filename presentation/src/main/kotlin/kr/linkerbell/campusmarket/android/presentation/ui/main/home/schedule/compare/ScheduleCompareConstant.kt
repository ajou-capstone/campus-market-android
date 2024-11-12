package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.ScheduleConstant

object ScheduleCompareConstant {
    const val ROUTE = "${ScheduleConstant.ROUTE}/compare"
    const val ROUTE_ARGUMENT_USER_ID = "user_id"
    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_USER_ID={$ROUTE_ARGUMENT_USER_ID}"
}
