package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.UserReportCategoryList

@Serializable
data class UserReportCategoryListRes(
    @SerialName("categories")
    val categoryList: List<String>,
) : DataMapper<UserReportCategoryList> {
    override fun toDomain(): UserReportCategoryList {
        return UserReportCategoryList(
            categoryList = categoryList
        )
    }
}
