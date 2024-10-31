package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorRes(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String
)
