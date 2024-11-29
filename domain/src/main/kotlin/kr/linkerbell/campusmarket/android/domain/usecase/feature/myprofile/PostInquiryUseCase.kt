package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class PostInquiryUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        title: String,
        category: String,
        description: String

    ): Result<Unit> {
        return myPageRepository.postInquiry(
            title = title,
            category = category,
            description = description
        )
    }
}
