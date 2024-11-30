package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetMyKeywordListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<List<Keyword>> {
        return myPageRepository.getMyKeywordList()
    }
}
