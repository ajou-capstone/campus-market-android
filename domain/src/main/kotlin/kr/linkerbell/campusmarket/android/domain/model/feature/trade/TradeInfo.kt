package kr.linkerbell.campusmarket.android.domain.model.feature.trade

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
) {
    companion object {
        val empty = TradeInfo(
            itemId = 0,
            userId = 0,
            campusId = 0,
            nickname = "dummy user",
            title = "title",
            description = "description",
            price = 0,
            category = "OTHER",
            thumbnail = "",
            images = emptyList(),
            chatCount = 0,
            likeCount = 0,
            isLiked = false,
            isSold = false,
        )
    }
}
