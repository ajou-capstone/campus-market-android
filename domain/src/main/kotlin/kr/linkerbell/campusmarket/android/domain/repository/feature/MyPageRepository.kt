package kr.linkerbell.campusmarket.android.domain.repository.feature

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryCategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
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

    suspend fun getInquiryList(): Flow<PagingData<UserInquiry>>

    suspend fun getInquiryInfo(qaId: Long): Result<InquiryInfo>

    suspend fun getMyKeywordList(): Result<List<Keyword>>

    suspend fun postNewKeyword(keywordName: String): Result<Unit>

    suspend fun deleteKeyword(keywordId: Long): Result<Unit>

    suspend fun deleteAllNotification(): Result<Unit>

    suspend fun getNotificationHistory(): Flow<PagingData<UserNotification>>

    suspend fun deleteNotificationById(notificationId: Long): Result<Unit>
}
