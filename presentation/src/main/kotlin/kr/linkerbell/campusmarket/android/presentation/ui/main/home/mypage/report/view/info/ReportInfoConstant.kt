package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.view.info

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.view.list.ReportListConstant

object ReportInfoConstant {
    const val ROUTE: String = "${ReportListConstant.ROUTE}/info"

    const val ROUTE_ARGUMENT_QA_ID = "qaId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_QA_ID={$ROUTE_ARGUMENT_QA_ID}"
}
