package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview

@Serializable
data class UserReviewRes(
    @SerialName("reviewList")
    val reviewList: List<ReviewItemRes>,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("hasPrevious")
    val hasPrevious: Boolean,
    @SerialName("hasNext")
    val hasNext: Boolean,
)

@Serializable
data class ReviewItemRes(
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("description")
    val description: String,
    @SerialName("rating")
    val rating: Double,
    @SerialName("createdAt")
    val createdAt: LocalDateTime
) : DataMapper<UserReview> {
    override fun toDomain(): UserReview {
        return UserReview(
            userId = userId,
            nickname = nickname,
            profileImage = profileImage,
            description = description,
            rating = rating.toInt(),
            createdAt = createdAt
        )
    }
}
