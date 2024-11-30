package kr.linkerbell.campusmarket.android.domain.model.feature.mypage

data class UserNotification(
    val notificationHistoryId: Long,
    val title: String,
    val description: String,
    val deeplink: String
) {
    companion object {
        val empty = UserNotification(
            notificationHistoryId = 0L,
            title = "",
            description = "",
            deeplink = ""

        )
    }
}
