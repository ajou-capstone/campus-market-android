package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.list

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.SummarizedUserReport

@Immutable
data class ReportListViewData(
    val userReportList: LazyPagingItems<SummarizedUserReport>
)
