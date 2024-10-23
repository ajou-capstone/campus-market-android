package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile

@Serializable
data class GetMyProfileRes(
    @SerialName("userId")
    val id: Long,
    @SerialName("campusId")
    val campusId: Long = -1L,
    @SerialName("loginEmail")
    val loginEmail: String,
    @SerialName("schoolEmail")
    val schoolEmail: String = "",
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("profileImage")
    val profileImage: String = "",
    @SerialName("rating")
    val rating: Double = 0.0
) : DataMapper<MyProfile> {
    override fun toDomain(): MyProfile {
        return MyProfile(
            id = id,
            campusId = campusId,
            loginEmail = loginEmail,
            schoolEmail = schoolEmail,
            nickname = nickname,
            profileImage = profileImage,
            rating = rating
        )
    }
}
