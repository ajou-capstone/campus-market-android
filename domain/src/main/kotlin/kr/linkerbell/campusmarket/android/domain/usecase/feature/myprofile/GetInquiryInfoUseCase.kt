package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetInquiryInfoUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(qaId: Long): Result<InquiryInfo> {
        return myPageRepository.getInquiryInfo(qaId)
    }
}
