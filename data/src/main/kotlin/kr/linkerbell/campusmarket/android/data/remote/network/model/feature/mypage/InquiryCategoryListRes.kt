package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryCategoryList

@Serializable
data class InquiryCategoryListRes(
    @SerialName("categories")
    val categoryList: List<String>,
) : DataMapper<InquiryCategoryList> {
    override fun toDomain(): InquiryCategoryList {
        return InquiryCategoryList(
            categoryList = categoryList
        )
    }
}
