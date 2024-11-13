package kr.linkerbell.campusmarket.android.domain.model.feature.myprofile


data class Review(
    val userId: Long,
    val nickname: String,
    val profileImage: String,
    val description: String,
    val rating: Double
) {
    companion object {
        val empty = Review(
            userId = 0L,
            nickname = "Empty",
            profileImage = "",
            description = "no description",
            rating = 0.0
        )
    }
}
