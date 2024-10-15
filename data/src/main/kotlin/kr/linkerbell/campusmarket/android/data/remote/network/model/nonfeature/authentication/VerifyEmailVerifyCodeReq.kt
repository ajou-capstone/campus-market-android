package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailVerifyCodeReq(
    @SerialName("token")
    val token: String,
    @SerialName("verifyCode")
    val verifyCode: String
)
