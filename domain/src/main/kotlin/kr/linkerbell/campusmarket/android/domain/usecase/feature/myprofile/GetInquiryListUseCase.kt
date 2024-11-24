package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetInquiryListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Flow<PagingData<UserInquiry>> {
        return myPageRepository.getInquiryList()
    }
}
