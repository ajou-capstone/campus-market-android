package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper

@Serializable
data class GetRecentMessageIdListRes(
    @SerialName("messageIdList")
    val messageIdList: List<Long>
) : DataMapper<List<Long>> {
    override fun toDomain(): List<Long> {
        return messageIdList
    }
}
