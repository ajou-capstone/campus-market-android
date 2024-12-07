package kr.linkerbell.campusmarket.android.domain.model.feature.trade

import kotlinx.datetime.LocalDateTime

data class TradeInfo(
    val itemId: Long,
    val userId: Long,
    val campusId: Long,
    val nickname: String,
    val title: String,
    val description: String,
    val price: Int,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
    val chatCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val isSold: Boolean,
    val createdDate: LocalDateTime,
    val lastModifiedDate: LocalDateTime,
) {
    companion object {
        val empty = TradeInfo(
            itemId = 0,
            userId = 0,
            campusId = 0,
            nickname = "알 수 없는 사용자",
            title = "제목 없음",
            description = "내용 없음",
            price = 0,
            category = "OTHER",
            thumbnail = "",
            images = emptyList(),
            chatCount = 0,
            likeCount = 0,
            isLiked = false,
            isSold = false,
            createdDate = LocalDateTime(2000, 1, 1, 0, 0, 0),
            lastModifiedDate = LocalDateTime(2000, 1, 1, 0, 0, 0)
        )
    }
}
