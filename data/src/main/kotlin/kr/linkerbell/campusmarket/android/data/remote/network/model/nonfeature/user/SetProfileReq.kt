package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetProfileReq(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String
)
