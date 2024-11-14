package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageReq {

    @Serializable
    data class Text(
        @SerialName("content")
        val content: String,
        @SerialName("contentType")
        val contentType: String
    ) : MessageReq

    @Serializable
    data class Image(
        @SerialName("content")
        val content: String,
        @SerialName("contentType")
        val contentType: String
    ) : MessageReq

    @Serializable
    data class Schedule(
        @SerialName("contentType")
        val contentType: String
    ) : MessageReq
}
