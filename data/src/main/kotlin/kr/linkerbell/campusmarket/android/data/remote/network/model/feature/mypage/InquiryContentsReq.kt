package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InquiryContentsReq(
    @SerialName("title")
    val title: String,
    @SerialName("category")
    val category: String,
    @SerialName("description")
    val description: String
)
