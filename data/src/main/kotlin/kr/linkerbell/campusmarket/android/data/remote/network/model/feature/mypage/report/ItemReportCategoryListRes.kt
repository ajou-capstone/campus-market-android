package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ItemReportCategoryList

@Serializable
data class ItemReportCategoryListRes(
    @SerialName("categories")
    val categoryList: List<String>,
) : DataMapper<ItemReportCategoryList> {
    override fun toDomain(): ItemReportCategoryList {
        return ItemReportCategoryList(
            categoryList = categoryList
        )
    }
}
