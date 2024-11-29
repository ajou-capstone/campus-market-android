package kr.linkerbell.campusmarket.android.data.repository.feature.mypage

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.data.common.DEFAULT_PAGING_SIZE
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.MyPageApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.InquiryPagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.MyLikesPagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.RecentTradePagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.ReviewPagingSource
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryCategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

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

    override suspend fun getInquiryCategoryList(): Result<InquiryCategoryList> {
        return myPageApi.getInquiryCategory().toDomain()
    }

    override suspend fun postInquiry(
        title: String,
        category: String,
        description: String
    ): Result<Unit> {
        return myPageApi.postInquiry(title, category, description)
    }

    override suspend fun getInquiryList(): Flow<PagingData<UserInquiry>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                InquiryPagingSource(
                    myPageApi = myPageApi
                )
            },
        ).flow
    }

    override suspend fun getInquiryInfo(qaId: Long): Result<InquiryInfo> {
        return myPageApi.getInquiryInfo(qaId).toDomain()
    }

    override suspend fun getMyLikes(): Flow<PagingData<SummarizedTrade>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                MyLikesPagingSource(
                    myPageApi = myPageApi
                )
            },
        ).flow
    }
}
