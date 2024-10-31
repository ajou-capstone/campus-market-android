package kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication.SendEmailVerifyCodeReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication.SendEmailVerifyCodeRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication.VerifyEmailVerifyCodeReq
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class AuthenticationApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun sendEmailVerifyCode(
        email: String
    ): Result<SendEmailVerifyCodeRes> {
        return client.post("$baseUrl/api/v1/auth/emails/send") {
            setBody(
                SendEmailVerifyCodeReq(
                    email = email
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun verifyEmailVerifyCode(
        token: String,
        verifyCode: String
    ): Result<Unit> {
        return client.post("$baseUrl/api/v1/auth/emails/verify") {
            setBody(
                VerifyEmailVerifyCodeReq(
                    token = token,
                    verifyCode = verifyCode
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun logout(): Result<Unit> {
        return client.post("$baseUrl/api/v1/auth/logout")
            .convert(errorMessageMapper::map)
    }

    suspend fun withdraw(): Result<Unit> {
        return client.patch("$baseUrl/api/v1/auth/withdraw")
            .convert(errorMessageMapper::map)
    }
}
