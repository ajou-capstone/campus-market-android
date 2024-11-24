package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryCategoryList
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetInquiryCategoryListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<InquiryCategoryList> {
        return myPageRepository.getInquiryCategoryList()
    }
}
