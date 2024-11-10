package kr.linkerbell.campusmarket.android.domain.model.feature.trade

data class LikedItemInfo(
    val likeId: Long,
    val itemId: Long,
    val isLike: Boolean
) {
    companion object {
        val empty = LikedItemInfo(
            likeId = 0,
            itemId = 0,
            isLike = false
        )
    }
}
