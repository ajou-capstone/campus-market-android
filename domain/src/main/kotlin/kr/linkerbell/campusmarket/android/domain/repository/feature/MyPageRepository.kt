package kr.linkerbell.campusmarket.android.domain.repository.feature

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
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

interface MyPageRepository {

    suspend fun getUserReviews(
        userId: Long
    ): Flow<PagingData<UserReview>>

    suspend fun getRecentTrades(
        userId: Long,
        type: String
    ): Flow<PagingData<RecentTrade>>

    suspend fun getMyLikes(): Flow<PagingData<SummarizedTrade>>

    suspend fun getInquiryCategoryList(): Result<InquiryCategoryList>

    suspend fun postInquiry(title: String, category: String, description: String): Result<Unit>

    suspend fun getReportList(): Flow<PagingData<SummarizedUserReport>>

    suspend fun getReportInfo(qaId: Long): Result<ReportInfo>

    suspend fun getMyKeywordList(): Result<List<Keyword>>

    suspend fun postNewKeyword(keywordName: String): Result<Unit>

    suspend fun deleteKeyword(keywordId: Long): Result<Unit>

    suspend fun deleteAllNotification(): Result<Unit>

    suspend fun getNotificationHistory(): Flow<PagingData<UserNotification>>

    suspend fun deleteNotificationById(notificationId: Long): Result<Unit>

    suspend fun getItemReportCategoryList(): Result<ItemReportCategoryList>

    suspend fun getUserReportCategoryList(): Result<UserReportCategoryList>

    suspend fun postItemReport(itemId: Long, category: String, description: String): Result<Unit>

    suspend fun postUserReport(userId: Long, category: String, description: String): Result<Unit>
}
