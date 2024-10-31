package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message

sealed interface MessageReq {

    @Serializable
    data class Text(
        @SerialName("content")
        val content: String,
        @SerialName("contentType")
        val contentType: String = "TEXT"
    ) : MessageReq

    @Serializable
    data class Image(
        @SerialName("content")
        val content: String,
        @SerialName("contentType")
        val contentType: String = "IMAGE"
    ) : MessageReq
}

fun Message.toRequest(): MessageReq {
    return when (this) {
        is Message.Text -> MessageReq.Text(content = content)
        is Message.Image -> MessageReq.Image(content = content)
    }
}
