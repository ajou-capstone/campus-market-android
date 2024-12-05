package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade

@Serializable
data class RecentTradeRes(
    @SerialName("content")
    val content: List<RecentTradeItemRes>,
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
data class RecentTradeSortRes(
    @SerialName("empty")
    val empty: Boolean,
    @SerialName("sorted")
    val sorted: Boolean,
    @SerialName("unsorted")
    val unsorted: Boolean,
)

@Serializable
data class RecentTradeItemRes(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("price")
    val price: Int,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("itemStatus")
    val itemStatus: String,
    @SerialName("createdAt")
    val createdAt: LocalDateTime,
    @SerialName("modifiedAt")
    val modifiedAt: LocalDateTime,
    @SerialName("isReviewed")
    val isReviewed: Boolean,
) : DataMapper<RecentTrade> {
    override fun toDomain(): RecentTrade {
        return RecentTrade(
            itemId = itemId,
            title = title,
            userId = userId,
            nickname = nickname,
            price = price,
            thumbnail = thumbnail,
            isSold = (itemStatus == "SOLDOUT"),
            createdAt = createdAt,
            modifiedAt = modifiedAt,
            isReviewed = isReviewed
        )
    }
}
