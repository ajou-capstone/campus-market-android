package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Serializable
data class GetUserProfileRes(
    @SerialName("id")
    val id: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("rating")
    val rating: Double
) : DataMapper<UserProfile> {
    override fun toDomain(): UserProfile {
        return UserProfile(
            id = id,
            nickname = nickname,
            profileImage = profileImage,
            rating = rating
        )
    }
}
