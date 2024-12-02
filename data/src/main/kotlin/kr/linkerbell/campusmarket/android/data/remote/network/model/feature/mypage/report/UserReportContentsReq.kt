package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserReportContentsReq(
    @SerialName("userId")
    val userId: Long,
    @SerialName("category")
    val category: String,
    @SerialName("description")
    val description: String
)
