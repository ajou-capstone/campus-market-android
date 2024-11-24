package kr.linkerbell.campusmarket.android.domain.model.feature.mypage

import kotlinx.datetime.LocalDateTime

data class UserInquiry(
    val qaId: Long,
    val userId: Long,
    val category: String,
    val title: String,
    val isCompleted: Boolean,
    val createdDate: LocalDateTime,
    val answerDate: LocalDateTime
)
