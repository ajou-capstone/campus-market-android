package kr.linkerbell.campusmarket.android.domain.model.nonfeature.item

data class SummarizedItem(
    val itemId: Long,
    val userId: Long,
    val nickname : String,
    val thumbnail: String,
    val title: String,
    val price: Int,
    val chatCount: Int,
    val likeCount: Int,
    val itemStatus: String
) {
    companion object {
        val empty = SummarizedItem(
            itemId = 0,
            userId = 0,
            nickname = "User Nickname",
            thumbnail = "",
            title = "Empty Title",
            price = 100,
            chatCount = 0,
            likeCount = 0,
            itemStatus = ""
        )
    }
}
