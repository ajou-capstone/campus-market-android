package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.DeletedLikedItemInfo

@Serializable
data class DeletedLikedItemRes(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("isLike")
    val isLike: Boolean
) : DataMapper<DeletedLikedItemInfo> {
    override fun toDomain(): DeletedLikedItemInfo {
        return DeletedLikedItemInfo(
            itemId = itemId,
            isLike = isLike
        )
    }
}
