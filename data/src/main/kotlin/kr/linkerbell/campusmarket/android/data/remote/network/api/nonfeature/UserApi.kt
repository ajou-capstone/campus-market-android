package kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.GetProfileRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.SetProfileReq
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert
import javax.inject.Inject

class UserApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun setProfile(
        nickname: String,
        profileImage: String
    ): Result<Unit> {
        return client.post("$baseUrl/api/v1/profile/me") {
            setBody(
                SetProfileReq(
                    nickname = nickname,
                    profileImage = profileImage
                )
            )
        }
            .convert(errorMessageMapper::map)
    }

    suspend fun getProfile(): Result<GetProfileRes> {
        return client.get("$baseUrl/api/v1/profile/me")
            .convert(errorMessageMapper::map)
    }
}
