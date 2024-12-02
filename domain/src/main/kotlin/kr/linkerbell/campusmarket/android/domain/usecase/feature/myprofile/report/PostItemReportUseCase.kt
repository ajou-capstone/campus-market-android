package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class PostItemReportUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        itemId: Long,
        category: String,
        description: String
    ): Result<Unit> {
        return myPageRepository.postItemReport(
            itemId = itemId,
            category = category,
            description = description
        )
    }
}
