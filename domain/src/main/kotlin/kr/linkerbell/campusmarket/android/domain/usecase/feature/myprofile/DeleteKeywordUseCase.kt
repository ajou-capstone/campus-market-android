package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class DeleteKeywordUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        keywordId: Long
    ): Result<Unit> {
        return myPageRepository.deleteKeyword(
            keywordId = keywordId
        )
    }
}
