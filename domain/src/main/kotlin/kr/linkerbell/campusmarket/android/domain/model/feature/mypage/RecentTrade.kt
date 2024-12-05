package kr.linkerbell.campusmarket.android.domain.model.feature.mypage

import kotlinx.datetime.LocalDateTime

data class RecentTrade(
    val id: Long,
    val title: String,
    val price: Int,
    val thumbnail: String,
    val isSold: Boolean,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
    val isReviewed: Boolean,
)
