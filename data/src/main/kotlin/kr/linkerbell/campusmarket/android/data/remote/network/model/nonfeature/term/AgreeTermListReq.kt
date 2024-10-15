package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.term

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgreeTermListReq(
    @SerialName("terms")
    val terms: List<AgreeTermListItemReq>
)

@Serializable
data class AgreeTermListItemReq(
    @SerialName("termId")
    val id: Long,
    @SerialName("isAgree")
    val isAgree: Boolean
)
