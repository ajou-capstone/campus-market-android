package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class PostNewKeywordUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        keywordName: String
    ): Result<Unit> {
        return myPageRepository.postNewKeyword(
            keywordName = keywordName
        )
    }
}
