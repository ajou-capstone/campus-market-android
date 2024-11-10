package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo

@Serializable
data class TradeInfoRes(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("campusId")
    val campusId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Int,
    @SerialName("category")
    val category: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("images")
    val images: List<String>,
    @SerialName("chatCount")
    val chatCount: Int,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("itemStatus")
    val itemStatus: String
) : DataMapper<TradeInfo> {
    override fun toDomain(): TradeInfo {
        val isSold = when (itemStatus) {
            "FORSALE" -> false
            "SOLDOUT" -> true
            else -> false
        }
        return TradeInfo(
            itemId = itemId,
            userId = userId,
            campusId = campusId,
            nickname = nickname,
            title = title,
            description = description,
            price = price,
            category = category,
            thumbnail = thumbnail,
            images = images,
            chatCount = chatCount,
            likeCount = likeCount,
            isLiked = isLiked,
            isSold = isSold
        )
    }
}
