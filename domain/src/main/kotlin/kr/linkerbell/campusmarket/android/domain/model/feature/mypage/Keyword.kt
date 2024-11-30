package kr.linkerbell.campusmarket.android.domain.model.feature.mypage

data class Keyword(
    val id: Long,
    val keyword: String
) {
    companion object {
        val empty = Keyword(
            id = 0L,
            keyword = ""
        )
    }
}
