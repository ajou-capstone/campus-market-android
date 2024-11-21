package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.CategoryListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.ChangeTradeStatusReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.DeletedLikedItemRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.PostLikedItemRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.PostTradeReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.PostTradeRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.SearchTradeListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.TradeInfoRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.UserRatingReq
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import javax.inject.Inject

class TradeApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun searchTradeList(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String,
        pageNum: Int,
        pageSize: Int
    ): Result<SearchTradeListRes> {
        return client.get("$baseUrl/api/v1/items") {
            parameter("name", name)
            parameter("category", category)
            parameter("minPrice", minPrice.toString())
            parameter("maxPrice", maxPrice.toString())
            parameter("sort", sorted)
            parameter("page", pageNum.toString())
            parameter("size", pageSize.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun postNewTrade(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnail: String,
        images: List<String>
    ): Result<PostTradeRes> {
        return client.post("$baseUrl/api/v1/items") {
            setBody(
                PostTradeReq(
                    title = title,
                    description = description,
                    price = price,
                    category = category,
                    thumbnail = thumbnail,
                    images = images
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun patchTradeContents(
        tradeContents: TradeContents,
        itemId: Long
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/items/$itemId") {
            setBody(
                PostTradeReq(
                    title = tradeContents.title,
                    description = tradeContents.description,
                    price = tradeContents.price,
                    category = tradeContents.category,
                    thumbnail = tradeContents.thumbnail,
                    images = tradeContents.images
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun changeTradeStatus(
        itemStatus: String,
        itemId: Long,
        buyerId: Long
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/items/$itemId/change-status") {
            setBody(
                ChangeTradeStatusReq(
                    itemStatus = itemStatus,
                    buyerId = buyerId
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun getCategoryList(): Result<CategoryListRes> {
        return client.get("$baseUrl/api/v1/items/categories")
            .convert(errorMessageMapper::map)
    }

    suspend fun getTradeInfo(itemId: Long): Result<TradeInfoRes> {
        return client.get("$baseUrl/api/v1/items/$itemId")
            .convert(errorMessageMapper::map)
    }


    suspend fun postLikeItem(itemId: Long): Result<PostLikedItemRes> {
        return client.post("$baseUrl/api/v1/items/$itemId/likes")
            .convert(errorMessageMapper::map)
    }

    suspend fun deleteLikedItem(itemId: Long): Result<DeletedLikedItemRes> {
        return client.delete("$baseUrl/api/v1/items/$itemId/likes")
            .convert(errorMessageMapper::map)
    }

    suspend fun deleteTradeInfo(itemId: Long): Result<Unit> {
        return client.delete("$baseUrl/api/v1/items/$itemId")
            .convert(errorMessageMapper::map)
    }

    suspend fun postUserRating(
        targetUserId: Long,
        itemId: Long,
        description: String,
        rating: Int
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/users/{$targetUserId}/reviews") {
            setBody(
                UserRatingReq(
                    itemId = itemId,
                    description = description,
                    rating = rating
                )
            )
        }.convert(errorMessageMapper::map)
    }
}
