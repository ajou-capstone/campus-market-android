package kr.linkerbell.campusmarket.android.domain.model.nonfeature.user

data class UserProfile(
    val id: Long,
    val nickname: String,
    val profileImage: String,
    val rating: Double
) {
    companion object {
        val empty = UserProfile(
            id = -1,
            nickname = "",
            profileImage = "",
            rating = 0.0
        )
    }
}
