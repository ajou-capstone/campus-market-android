package kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile

data class RandomUserProfile(
    val fullName: String,
    val gender: String,
    val email: String,
) {
    companion object {
        val empty = RandomUserProfile(
            fullName = "Unknown",
            gender = "Male",
            email = "Unknown",
        )
    }
}
