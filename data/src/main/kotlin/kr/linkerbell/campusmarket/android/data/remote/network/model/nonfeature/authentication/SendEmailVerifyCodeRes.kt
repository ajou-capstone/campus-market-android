package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper

@Serializable
data class SendEmailVerifyCodeRes(
    @SerialName("token")
    val token: String
) : DataMapper<String> {
    override fun toDomain(): String {
        return token
    }
}
