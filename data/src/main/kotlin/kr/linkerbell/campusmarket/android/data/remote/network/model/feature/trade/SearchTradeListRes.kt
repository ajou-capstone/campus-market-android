package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade

@Serializable
data class SearchTradeListRes(
    @SerialName("data")
    val data: MutableList<SearchTradeListItemRes>
) : DataMapper<List<Trade>> {
    override fun toDomain(): List<Trade> {
        return data.map { it.toDomain() }
    }
}

@Serializable
data class SearchTradeListItemRes(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("price")
    val price: Int,
    @SerialName("chatCount")
    val chatCount: Int,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("itemStatus")
    val itemStatus: String
) : DataMapper<Trade> {
    override fun toDomain(): Trade {
        return Trade(
            itemId = itemId,
            userId = userId,
            nickname = nickname,
            thumbnail = thumbnail,
            title = title,
            price = price,
            chatCount = chatCount,
            likeCount = likeCount,
            itemStatus = itemStatus
        )
    }
}
