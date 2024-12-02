package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.UserReportCategoryList
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetUserReportCategoryListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<UserReportCategoryList> {
        return myPageRepository.getUserReportCategoryList()
    }
}
