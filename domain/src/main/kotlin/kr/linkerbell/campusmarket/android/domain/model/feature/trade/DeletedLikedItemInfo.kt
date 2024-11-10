package kr.linkerbell.campusmarket.android.domain.model.feature.trade

data class DeletedLikedItemInfo(
    val itemId: Long,
    val isLike: Boolean
) {
    companion object {
        val empty = DeletedLikedItemInfo(
            itemId = 0,
            isLike = false
        )
    }
}
