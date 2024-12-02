package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.item

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.MyPageConstant

object ItemReportConstant {
    const val ROUTE = "${MyPageConstant.ROUTE}/report/Item"

    const val ROUTE_ARGUMENT_ITEM_ID = "itemId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?${ROUTE_ARGUMENT_ITEM_ID}={${ROUTE_ARGUMENT_ITEM_ID}}"
}
