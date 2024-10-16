package kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.Parameters
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.item.SummarizedItemListRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.ItemQueryParameter


class SummarizedItemsListApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getSummarizedItem(query: ItemQueryParameter)
            : Result<SummarizedItemListRes> {

        val client = HttpClient()
        val queryParameters = Parameters.build {
            append("name", query.name)
            append("category", query.category)
            append("minPrice", query.minPrice.toString())
            append("maxPrice", query.maxPrice.toString())
            append("sorted", query.sorted)
            append("pageNum", query.pageNum.toString())
            append("pageSize", query.pageSize.toString())
        }

        return client.get("$baseUrl/api/v1/items") {
            url {
                parameters.appendAll(queryParameters)
            }
        }.convert(errorMessageMapper::map)
    }
}
