package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAccessTokenRes(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String
)
