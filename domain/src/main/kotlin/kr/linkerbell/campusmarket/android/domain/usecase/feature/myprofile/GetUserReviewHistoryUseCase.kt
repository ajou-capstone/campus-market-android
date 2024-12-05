package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetUserReviewHistoryUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Flow<PagingData<UserReview>> {
        return myPageRepository.getUserReviewHistory()
    }
}
