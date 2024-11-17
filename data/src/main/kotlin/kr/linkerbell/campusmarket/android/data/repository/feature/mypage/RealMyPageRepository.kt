package kr.linkerbell.campusmarket.android.data.repository.feature.mypage

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.data.common.DEFAULT_PAGING_SIZE
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.MyPageApi
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.RecentTradePagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.ReviewPagingSource
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository
import javax.inject.Inject

class RealMyPageRepository @Inject constructor(
    private val myPageApi: MyPageApi,
) : MyPageRepository {

    override suspend fun getUserReviews(userId: Long): Flow<PagingData<UserReview>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                ReviewPagingSource(
                    myPageApi = myPageApi,
                    userId = userId
                )
            },
        ).flow
    }

    override suspend fun getRecentTrades(
        userId: Long,
        type: String
    ): Flow<PagingData<RecentTrade>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                RecentTradePagingSource(
                    myPageApi = myPageApi,
                    userId = userId,
                    type = type
                )
            },
        ).flow
    }
}
