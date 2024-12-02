package kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report

data class InquiryCategoryList(
    val categoryList: List<String>
) {
    companion object {
        val empty = InquiryCategoryList(
            categoryList = listOf(
                "ACCOUNT_INQUIRY",
                "CHAT_AND_NOTIFICATION",
                "SECONDHAND_TRANSACTION",
                "ADVERTISEMENT_INQUIRY",
                "OTHER"
            )
        )
    }
}
