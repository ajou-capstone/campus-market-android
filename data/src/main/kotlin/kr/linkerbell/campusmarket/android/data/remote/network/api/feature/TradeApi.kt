package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.CategoryListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade.SearchTradeListRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

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
            parameter("pageNum", pageNum.toString())
            parameter("pageSize", pageSize.toString())
        }.convert(errorMessageMapper::map)
    }

    suspend fun getCategoryList(): Result<CategoryListRes> {
        return client.get("$baseUrl/api/v1/items/categories").convert(errorMessageMapper::map)
    }
}
