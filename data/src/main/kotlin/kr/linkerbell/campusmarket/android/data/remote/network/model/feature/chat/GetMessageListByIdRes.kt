package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message

@Serializable
@SerialName("TEXT")
data class GetMessageListByIdRes(
    @SerialName("data")
    val data: List<MessageRes>
) : DataMapper<List<Message>> {
    override fun toDomain(): List<Message> {
        return data.map { it.toDomain() }
    }
}
