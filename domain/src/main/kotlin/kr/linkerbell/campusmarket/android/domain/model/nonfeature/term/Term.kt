package kr.linkerbell.campusmarket.android.domain.model.nonfeature.term

data class Term(
    val id: Long,
    val title: String,
    val url: String,
    val isRequired: Boolean,
    val isAgree: Boolean,
)
