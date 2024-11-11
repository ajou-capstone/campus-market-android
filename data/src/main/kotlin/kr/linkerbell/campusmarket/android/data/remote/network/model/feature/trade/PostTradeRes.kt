package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper

data class PostTradeRes (
    @SerialName("itemId")
    val itemId: Long
) : DataMapper<Long> {
    override fun toDomain(): Long {
        return itemId
    }
}

