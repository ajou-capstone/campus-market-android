package kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.GetAvailableCampusListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.GetMyProfileRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.GetUserProfileRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.SetCampusReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user.SetProfileReq
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class UserApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun setMyProfile(
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
        }.convert(errorMessageMapper::map)
    }

    suspend fun getMyProfile(): Result<GetMyProfileRes> {
        return client.get("$baseUrl/api/v1/profile/me")
            .convert(errorMessageMapper::map)
    }

    suspend fun getAvailableCampusList(): Result<GetAvailableCampusListRes> {
        return client.get("$baseUrl/api/v1/profile/campus")
            .convert(errorMessageMapper::map)
    }

    suspend fun setCampus(
        id: Long
    ): Result<Unit> {
        return client.post("$baseUrl/api/v1/profile/campus") {
            setBody(
                SetCampusReq(
                    campusId = id
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun getUserProfile(
        id: Long
    ): Result<GetUserProfileRes> {
        return client.get("$baseUrl/api/v1/profile/$id").convert(errorMessageMapper::map)
    }
}
