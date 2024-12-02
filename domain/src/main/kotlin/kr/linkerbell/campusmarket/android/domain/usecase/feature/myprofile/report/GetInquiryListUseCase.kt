package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.SummarizedUserReport
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetInquiryListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Flow<PagingData<SummarizedUserReport>> {
        return myPageRepository.getReportList()
    }
}
