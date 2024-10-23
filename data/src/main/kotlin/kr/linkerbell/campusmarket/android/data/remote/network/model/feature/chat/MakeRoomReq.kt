package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MakeRoomReq(
    @SerialName("itemId")
    val tradeId: Long,
    @SerialName("userId")
    val userId: Long
)
