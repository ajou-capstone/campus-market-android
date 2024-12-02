package kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report

import kotlinx.datetime.LocalDateTime

data class SummarizedUserReport(
    val qaId: Long,
    val userId: Long,
    val category: String,
    val title: String,
    val isCompleted: Boolean,
    val createdDate: LocalDateTime,
    val answerDate: LocalDateTime
){
    companion object {
        val empty = SummarizedUserReport(
            qaId = 0L,
            userId = 0L,
            title = "",
            category = "",
            isCompleted = false,
            createdDate = LocalDateTime(2000, 1, 1, 0, 0, 0),
            answerDate = LocalDateTime(2000, 1, 1, 0, 0, 0)
        )
    }
}

