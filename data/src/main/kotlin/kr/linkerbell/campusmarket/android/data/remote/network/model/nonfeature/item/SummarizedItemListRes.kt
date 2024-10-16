package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem

@Serializable
data class SummarizedItemListRes(
    @SerialName("data")
    val data: MutableList<SummarizedItemElement>
) : DataMapper<MutableList<SummarizedItem>> {

    override fun toDomain(): MutableList<SummarizedItem> {
        return data.map { itemElement ->
            SummarizedItem(
                itemId = itemElement.itemId,
                userId = itemElement.userId,
                nickname = itemElement.nickname,
                thumbnail = itemElement.thumbnail,
                title = itemElement.title,
                price = itemElement.price,
                chatCount = itemElement.chatCount,
                likeCount = itemElement.likeCount,
                itemStatus = itemElement.itemStatus
            )
        }.toMutableList()
    }
}


@Serializable
data class SummarizedItemElement(
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
    @SerialName("itemStats")
    val itemStatus: String
) {

}
