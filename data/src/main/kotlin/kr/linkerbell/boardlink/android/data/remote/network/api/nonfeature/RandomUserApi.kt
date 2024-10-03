package kr.linkerbell.boardlink.android.data.remote.network.api.nonfeature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import javax.inject.Inject
import kr.linkerbell.boardlink.android.data.remote.network.di.NoAuthHttpClient
import kr.linkerbell.boardlink.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.boardlink.android.data.remote.network.model.nonfeature.randomuserprofile.RandomUserProfileRes
import kr.linkerbell.boardlink.android.data.remote.network.util.convert


class RandomUserApi @Inject constructor(
    @NoAuthHttpClient private val client: HttpClient,
    private val errorMessageMapper: ErrorMessageMapper
) {
    suspend fun getRandomUserProfile(): Result<RandomUserProfileRes> {
        return client.get("https://randomuser.me/api/?results")
            .convert(errorMessageMapper::map)
    }
}
