package kr.linkerbell.campusmarket.android.domain.model.feature.mypage

import kotlinx.datetime.LocalDateTime

data class UserReview(
    val userId: Long,
    val nickname: String,
    val profileImage: String,
    val description: String,
    val rating: Int,
    val createdAt: LocalDateTime
)
