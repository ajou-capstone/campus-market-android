package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRatingReq(
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("description")
    val description: String,
    @SerialName("rating")
    val rating: Int
)
