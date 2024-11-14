package kr.linkerbell.campusmarket.android.domain.model.feature.trade

data class TradeContents(
    val title: String,
    val description: String,
    val price: Int,
    val category: String,
    val thumbnail: String,
    val images: List<String>
) {
    companion object {
        val empty = TradeContents(
            title = "",
            description = "",
            price = 0,
            category = "OTHER",
            thumbnail = "",
            images = listOf("")
        )
    }
}
