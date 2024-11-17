package kr.linkerbell.campusmarket.android.domain.model.feature.mypage

data class RecentTrade(
    val id: Long,
    val title: String,
    val price: Int,
    val thumbnail: String,
    val isSold: Boolean
)