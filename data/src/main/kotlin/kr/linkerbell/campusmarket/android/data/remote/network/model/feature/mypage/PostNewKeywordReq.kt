package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostNewKeywordReq(
    @SerialName("keywordName")
    val keywordName: String
)
