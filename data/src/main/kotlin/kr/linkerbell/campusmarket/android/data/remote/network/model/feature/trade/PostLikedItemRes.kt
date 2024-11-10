package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.LikedItemInfo

@Serializable
data class PostLikedItemRes(
    @SerialName("likeId")
    val likeId: Long,
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("isLike")
    val isLike: Boolean
) : DataMapper<LikedItemInfo> {
    override fun toDomain(): LikedItemInfo {
        return LikedItemInfo(
            likeId = likeId,
            itemId = itemId,
            isLike = isLike
        )
    }
}
