package kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report

import kotlinx.datetime.LocalDateTime

data class ReportInfo(
    val qaId: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val answerDescription: String,
    val category: String,
    val isCompleted: Boolean,
    val createdDate: LocalDateTime,
    val answerDate: LocalDateTime
) {
    companion object {
        val empty = ReportInfo(
            qaId = 0L,
            userId = 0L,
            title = "",
            description = "",
            answerDescription = "",
            category = "",
            isCompleted = false,
            createdDate = LocalDateTime(2000, 1, 1, 0, 0, 0),
            answerDate = LocalDateTime(2000, 1, 1, 0, 0, 0)
        )
    }
}
