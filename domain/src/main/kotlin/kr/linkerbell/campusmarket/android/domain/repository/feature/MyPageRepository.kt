package kr.linkerbell.campusmarket.android.domain.repository.feature

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview

interface MyPageRepository {

    suspend fun getUserReviews(
        userId: Long
    ): Flow<PagingData<UserReview>>

    suspend fun getRecentTrades(
        userId: Long,
        type: String
    ): Flow<PagingData<RecentTrade>>
}
