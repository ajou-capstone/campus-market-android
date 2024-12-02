package kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report

data class ItemReportCategoryList(
    val categoryList: List<String>
) {
    companion object {
        val empty = ItemReportCategoryList(
            categoryList = listOf(
                "PROHIBITED_ITEM",
                "NOT_SECONDHAND_POST",
                "COMMERCIAL_SELLER",
                "DISPUTE_DURING_TRANSACTION",
                "FRAUD",
                "OTHER"
            )
        )
    }
}
