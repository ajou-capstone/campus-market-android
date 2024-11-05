package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.category.CategoryListRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class CategoryApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getCategoryList(): Result<CategoryListRes> {
        return client.get("$baseUrl/api/v1/items/categories").convert(errorMessageMapper::map)
    }
}
