package kr.linkerbell.campusmarket.android.domain.model.nonfeature.item

data class ItemQueryParameter(
    val name: String,
    val category: String,
    val minPrice: Int,
    val maxPrice: Int,
    val sorted: String,
    val pageNum: Int,
    val pageSize: Int,
) {
    companion object {
        val empty = ItemQueryParameter(
            name = "",
            category = "",
            minPrice = 0,
            maxPrice = 1000000,
            sorted = "asc", // TODO : sorted type <- 협의 필요 (기본값)
            pageNum = 1,
            pageSize = 7
        )
    }
}
