package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade

@Serializable
data class SearchTradeListRes(
    @SerialName("content")
    val content: List<SearchTradeListItemRes>,
    @SerialName("sort")
    val sort: SearchTradeListSortRes,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("hasPrevious")
    val hasPrevious: Boolean,
    @SerialName("hasNext")
    val hasNext: Boolean,
)

@Serializable
data class SearchTradeListSortRes(
    @SerialName("sorted")
    val sorted: Boolean,
    @SerialName("empty")
    val direction: Boolean,
    @SerialName("unsorted")
    val orderProperty: Boolean,
)

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
    val itemStatus: String,
    @SerialName("isLiked")
    val isLiked: Boolean
) : DataMapper<SummarizedTrade> {
    override fun toDomain(): SummarizedTrade {
        return SummarizedTrade(
            itemId = itemId,
            userId = userId,
            nickname = nickname,
            thumbnail = thumbnail,
            title = title,
            price = price,
            chatCount = chatCount,
            likeCount = likeCount,
            itemStatus = itemStatus,
            isLiked = isLiked
        )
    }
}
