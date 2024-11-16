package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room

@Serializable
data class ChangeTradeStatusReq(
    @SerialName("itemStatus")
    val itemStatus: String,
    @SerialName("buyerId")
    val buyerId: Long
)
