package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.info

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view.InquiryViewConstant

object InquiryInfoConstant {
    const val ROUTE = "${InquiryViewConstant.ROUTE}"

    const val ROUTE_ARGUMENT_QA_ID = "qaId"

    const val ROUTE_STRUCTURE = ROUTE +
            "?$ROUTE_ARGUMENT_QA_ID={$ROUTE_ARGUMENT_QA_ID}"
}
