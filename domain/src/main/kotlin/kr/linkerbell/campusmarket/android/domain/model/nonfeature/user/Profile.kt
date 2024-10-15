package kr.linkerbell.campusmarket.android.domain.model.nonfeature.user

data class Profile(
    val userId: Long,
    val campusId: Long,
    val loginEmail: String,
    val schoolEmail: String,
    val nickname: String,
    val profileImage: String,
    val rating: Double
) {
    companion object {
        val empty = Profile(
            userId = 0,
            campusId = 0,
            loginEmail = "",
            schoolEmail = "",
            nickname = "",
            profileImage = "",
            rating = 0.0
        )
    }
}
