package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.RecentTradeRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.UserReviewRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class MyPageApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getUserReviews(
        userId: Long,
        page: Int,
        size: Int
    ): Result<UserReviewRes> {
        return client.get("$baseUrl/api/v1/users/$userId/reviews") {
            parameter("page", page.toString())
            parameter("size", size.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun getRecentTrade(
        userId: Long,
        page: Int,
        size: Int,
    ): Result<RecentTradeRes> {
        return client.get("$baseUrl/api/v1/items/$userId/history") {
            parameter("page", page.toString())
            parameter("size", size.toString())
            parameter("type", type.toString())
        }.convert(errorMessageMapper::map)
    }
}
