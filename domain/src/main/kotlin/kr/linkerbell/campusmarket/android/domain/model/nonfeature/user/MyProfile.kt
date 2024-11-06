package kr.linkerbell.campusmarket.android.domain.model.nonfeature.user

data class MyProfile(
    val id: Long,
    val campusId: Long,
    val loginEmail: String,
    val schoolEmail: String,
    val nickname: String,
    val profileImage: String,
    val rating: Double
) {
    companion object {
        val empty = MyProfile(
            id = -1,
            campusId = -1,
            loginEmail = "",
            schoolEmail = "",
            nickname = "",
            profileImage = "",
            rating = 0.0
        )
    }
}
