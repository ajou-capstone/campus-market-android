package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ItemReportCategoryList
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetItemReportCategoryListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<ItemReportCategoryList> {
        return myPageRepository.getItemReportCategoryList()
    }
}
