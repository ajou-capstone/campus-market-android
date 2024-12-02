package kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report

data class UserReportCategoryList(
    val categoryList: List<String>
) {
    companion object {
        val empty = UserReportCategoryList(
            categoryList = listOf(
                "OTHER",
                "FRAUD",
                "DISPUTE_DURING_TRANSACTION",
                "RUDE_USER",
                "PROFESSIONAL_SELLER",
                "DATING_PURPOSE_CHAT",
                "HATE_SPEECH",
                "INAPPROPRIATE_SEXUAL_BEHAVIOR"
            )
        )
    }
}
