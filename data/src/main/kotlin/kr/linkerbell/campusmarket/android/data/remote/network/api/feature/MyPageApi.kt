package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.KeywordListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.MyLikesRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.PostNewKeywordReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.RecentTradeRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.UserNotificationListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.UserReviewRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.InquiryCategoryListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.InquiryContentsReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.ItemReportCategoryListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.ItemReportContentsReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.ReportInfoRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.ReportListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.UserReportCategoryListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report.UserReportContentsReq
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class MyPageApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getReviewsToMe(
        userId: Long,
        page: Int,
        size: Int
    ): Result<UserReviewRes> {
        return client.get("$baseUrl/api/v1/users/$userId/reviews-to-me") {
            parameter("page", page.toString())
            parameter("size", size.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun getUserReviewHistory(
        page: Int,
        size: Int
    ): Result<UserReviewRes> {
        return client.get("$baseUrl/api/v1/users/reviews") {
            parameter("page", page.toString())
            parameter("size", size.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun getRecentTrade(
        userId: Long,
        page: Int,
        size: Int,
        type: String
    ): Result<RecentTradeRes> {
        return client.get("$baseUrl/api/v1/items/$userId/history") {
            parameter("page", page.toString())
            parameter("size", size.toString())
            parameter("type", type)
        }.convert(errorMessageMapper::map)
    }

    suspend fun getInquiryCategoryList(): Result<InquiryCategoryListRes> {
        return client.get("$baseUrl/api/v1/questions")
            .convert(errorMessageMapper::map)
    }

    suspend fun getItemReportCategoryList(): Result<ItemReportCategoryListRes> {
        return client.get("$baseUrl/api/v1/items/report")
            .convert(errorMessageMapper::map)
    }

    suspend fun getUserReportCategoryList(): Result<UserReportCategoryListRes> {
        return client.get("$baseUrl/api/v1/users/report")
            .convert(errorMessageMapper::map)
    }

    suspend fun postInquiry(
        title: String,
        category: String,
        description: String
    ): Result<Unit> {
        return client.post("$baseUrl/api/v1/questions") {
            setBody(
                InquiryContentsReq(
                    title = title,
                    category = category,
                    description = description
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun postItemReport(itemId: Long, category: String, description: String): Result<Unit> {
        return client.post("$baseUrl/api/v1/items/${itemId}/report") {
            setBody(
                ItemReportContentsReq(
                    itemId = itemId,
                    category = category,
                    description = description
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun postUserReport(userId: Long, category: String, description: String): Result<Unit> {
        return client.post("$baseUrl/api/v1/users/${userId}/report") {
            setBody(
                UserReportContentsReq(
                    userId = userId,
                    category = category,
                    description = description
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun getReportList(
        page: Int,
        size: Int
    ): Result<ReportListRes> {
        return client.get("$baseUrl/api/v1/answers") {
            parameter("page", page.toString())
            parameter("size", size.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun getReportInfo(qaId: Long): Result<ReportInfoRes> {
        return client.get("$baseUrl/api/v1/answers/${qaId}")
            .convert(errorMessageMapper::map)
    }

    suspend fun getMyLikes(
        page: Int,
        size: Int
    ): Result<MyLikesRes> {
        return client.get("$baseUrl/api/v1/items/likes") {
            parameter("page", page.toString())
            parameter("size", size.toString())
        }.convert<MyLikesRes>(errorMessageMapper::map)
    }

    suspend fun getMyKeywordList(): Result<KeywordListRes> {
        return client.get("$baseUrl/api/v1/keywords")
            .convert(errorMessageMapper::map)
    }

    suspend fun postNewKeyword(keywordName: String): Result<Unit> {
        return client.post("$baseUrl/api/v1/keywords") {
            setBody(
                PostNewKeywordReq(keywordName = keywordName)
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun deleteKeyword(keywordId: Long): Result<Unit> {
        return client.delete("$baseUrl/api/v1/keywords/${keywordId}")
            .convert(errorMessageMapper::map)
    }

    suspend fun deleteAllNotification(): Result<Unit> {
        return client.delete("$baseUrl/api/v1/notification-history")
            .convert(errorMessageMapper::map)
    }

    suspend fun getNotificationHistory(
        page: Int,
        size: Int
    ): Result<UserNotificationListRes> {
        return client.get("$baseUrl/api/v1/notification-history") {
            parameter("page", page.toString())
            parameter("size", size.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun deleteNotificationById(notificationId: Long): Result<Unit> {
        return client.delete("$baseUrl/api/v1/notification-history/${notificationId}")
            .convert(errorMessageMapper::map)
    }

}
