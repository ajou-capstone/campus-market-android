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
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.NotificationPagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.RecentTradePagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.ReviewHistoryPagingSource
import kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging.ReviewPagingSource
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.InquiryCategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ItemReportCategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ReportInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.SummarizedUserReport
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.UserReportCategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class RealMyPageRepository @Inject constructor(
    private val myPageApi: MyPageApi,
) : MyPageRepository {

    override suspend fun getReviewsToMe(userId: Long): Flow<PagingData<UserReview>> {
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

    override suspend fun getUserReviewHistory(): Flow<PagingData<UserReview>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                ReviewHistoryPagingSource(
                    myPageApi = myPageApi
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
        return myPageApi.getInquiryCategoryList().toDomain()
    }

    override suspend fun postInquiry(
        title: String,
        category: String,
        description: String
    ): Result<Unit> {
        return myPageApi.postInquiry(title, category, description)
    }

    override suspend fun getItemReportCategoryList(): Result<ItemReportCategoryList> {
        return myPageApi.getItemReportCategoryList().toDomain()
    }

    override suspend fun getUserReportCategoryList(): Result<UserReportCategoryList> {
        return myPageApi.getUserReportCategoryList().toDomain()
    }

    override suspend fun postItemReport(
        itemId: Long,
        category: String,
        description: String
    ): Result<Unit> {
        return myPageApi.postItemReport(itemId, category, description)
    }

    override suspend fun postUserReport(
        userId: Long,
        category: String,
        description: String
    ): Result<Unit> {
        return myPageApi.postUserReport(userId, category, description)
    }

    override suspend fun getReportList(): Flow<PagingData<SummarizedUserReport>> {
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

    override suspend fun getReportInfo(qaId: Long): Result<ReportInfo> {
        return myPageApi.getReportInfo(qaId).toDomain()
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

    override suspend fun getMyKeywordList(): Result<List<Keyword>> {
        return myPageApi.getMyKeywordList().toDomain()
    }

    override suspend fun postNewKeyword(keywordName: String): Result<Unit> {
        return myPageApi.postNewKeyword(keywordName)
    }

    override suspend fun deleteKeyword(keywordId: Long): Result<Unit> {
        return myPageApi.deleteKeyword(keywordId)
    }

    override suspend fun deleteAllNotification(): Result<Unit> {
        return myPageApi.deleteAllNotification()
    }

    override suspend fun getNotificationHistory(): Flow<PagingData<UserNotification>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                NotificationPagingSource(
                    myPageApi = myPageApi
                )
            },
        ).flow
    }

    override suspend fun deleteNotificationById(notificationId: Long): Result<Unit> {
        return myPageApi.deleteNotificationById(notificationId)
    }
}
