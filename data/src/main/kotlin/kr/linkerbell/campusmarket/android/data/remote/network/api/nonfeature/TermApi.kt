package kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.term.AgreeTermListItemReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.term.AgreeTermListReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.term.GetTermListRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class TermApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getTermList(): Result<GetTermListRes> {
        return client.get("$baseUrl/api/v1/terms")
            .convert(errorMessageMapper::map)
    }

    suspend fun agreeTermList(
        termIdList: List<Long>
    ): Result<Unit> {
        return client.post("$baseUrl/api/v1/terms/agreement") {
            setBody(
                AgreeTermListReq(
                    terms = termIdList.map {
                        AgreeTermListItemReq(
                            id = it,
                            isAgree = true
                        )
                    }
                )
            )
        }.convert(errorMessageMapper::map)
    }
}
