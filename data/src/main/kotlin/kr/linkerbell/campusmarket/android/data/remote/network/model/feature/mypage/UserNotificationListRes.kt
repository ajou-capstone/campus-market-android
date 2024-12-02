package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification

@Serializable
data class UserNotificationListRes(
    @SerialName("content")
    val content: List<UserNotificationListItemRes>,
    @SerialName("sort")
    val sort: RecentTradeSortRes,
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
data class UserNotificationListItemRes(
    @SerialName("notificationHistoryId")
    val notificationHistoryId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("deeplink")
    val deeplink: String,
) : DataMapper<UserNotification> {
    override fun toDomain(): UserNotification {
        return UserNotification(
            notificationHistoryId = notificationHistoryId,
            title = title,
            description = description,
            deeplink = deeplink
        )
    }
}

