package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class PostUserReportUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        userId: Long,
        category: String,
        description: String
    ): Result<Unit> {
        return myPageRepository.postUserReport(
            userId = userId,
            category = category,
            description = description
        )
    }
}
