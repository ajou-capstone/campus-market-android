package kr.linkerbell.campusmarket.android.domain.model.feature.trade

data class CategoryList(
    val categoryList: List<String>
) {
    companion object {
        val empty = CategoryList(
            categoryList = listOf(
                "ELECTRONICS_IT",
                "HOME_APPLIANCES",
                "FASHION_ACCESSORIES",
                "BOOKS_EDUCATIONAL_MATERIALS",
                "STATIONERY_OFFICE_SUPPLIES",
                "HOUSEHOLD_ITEMS",
                "KITCHEN_SUPPLIES",
                "FURNITURE_INTERIOR",
                "SPORTS_LEISURE",
                "ENTERTAINMENT_HOBBIES",
                "OTHER",
                ""
            )
        )
    }
}
