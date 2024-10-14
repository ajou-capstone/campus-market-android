package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginReq(
    @SerialName("idToken")
    val idToken: String,
    @SerialName("firebaseToken")
    val firebaseToken: String
)
