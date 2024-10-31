package kr.linkerbell.campusmarket.android.domain.model.nonfeature.user

data class UserProfile(
    val id: Long,
    val nickname: String,
    val profileImage: String,
    val rating: Double
)
