package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry

@Immutable
data class InquiryViewData(
    val inquiryList: LazyPagingItems<UserInquiry>
)
