package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade

@Serializable
data class MyLikesRes(
    @SerialName("content")
    val content: List<MyLikesContentRes>,
    @SerialName("sort")
    val sort: RecentTradeSortRes,
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
data class MyLikesContentRes(
    @SerialName("likeId")
    val likeId: Long,
    @SerialName("item")
    val item: MyLikesItemRes,
)

@Serializable
data class MyLikesItemRes(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("price")
    val price: Int,
    @SerialName("title")
    val title: String,
    @SerialName("chatCount")
    val chatCount: Int,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("itemStatus")
    val itemStatus: String,
    @SerialName("createdDate")
    val createdDate: LocalDateTime,
    @SerialName("lastModifiedDate")
    val lastModifiedDate: LocalDateTime
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
            isLiked = true,
            createdDate = createdDate,
            lastModifiedDate = lastModifiedDate
        )
    }
}
