package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class GetUserReviewUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        userId: Long
    ): Flow<PagingData<UserReview>> {
        return myPageRepository.getUserReviews(userId = userId)
    }
}
