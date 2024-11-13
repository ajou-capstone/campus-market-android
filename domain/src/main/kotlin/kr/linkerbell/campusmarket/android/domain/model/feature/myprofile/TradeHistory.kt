package kr.linkerbell.campusmarket.android.domain.model.feature.myprofile


data class TradeHistory(
    val id: Long,
    val title: String,
    val price: Int,
    val thumbnail: String
) {
    companion object {
        val empty = TradeHistory(
            id = 0,
            title = "Empty",
            price = 0,
            thumbnail = ""
        )
    }
}
