package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification

@Immutable
data class NotificationData(
    val notificationList: LazyPagingItems<UserNotification>
)
