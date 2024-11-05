package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.category

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList

@Serializable
data class CategoryListRes(
    @SerialName("categories")
    val categoryList: List<String>,
) : DataMapper<CategoryList> {
    override fun toDomain(): CategoryList {
        return CategoryList(
            categoryList = categoryList
        )
    }
}
