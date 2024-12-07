package kr.linkerbell.campusmarket.android.domain.model.feature.trade

import kotlinx.datetime.LocalDateTime

data class SummarizedTrade(
    val itemId: Long,
    val userId: Long,
    val nickname: String,
    val thumbnail: String,
    val title: String,
    val price: Int,
    val chatCount: Int,
    val likeCount: Int,
    val itemStatus: String,
    val isLiked: Boolean,
    val createdDate: LocalDateTime,
    val lastModifiedDate: LocalDateTime
) {
    companion object {
        val empty = SummarizedTrade(
            itemId = 0,
            userId = 0,
            nickname = "",
            thumbnail = "",
            title = "",
            price = 0,
            chatCount = 0,
            likeCount = 0,
            itemStatus = "",
            isLiked = false,
            createdDate = LocalDateTime(2000, 1, 1, 0, 0, 0),
            lastModifiedDate = LocalDateTime(2000, 1, 1, 0, 0, 0)
        )
    }
}
