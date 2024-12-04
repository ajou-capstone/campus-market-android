package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.DOMAIN
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox

@Composable
fun NotificationScreen(
    navController: NavController,
    argument: NotificationArgument,
    data: NotificationData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val notificationList by remember { mutableStateOf(data.notificationList) }

    ConstraintLayout(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        val (topBar, contents) = createRefs()
        Row(
            modifier = Modifier
                .height(Space56)
                .fillMaxWidth()
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RippleBox(
                modifier = Modifier
                    .padding(4.dp),
                onClick = {
                    navController.safeNavigateUp()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Navigate Up Button",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(horizontal = 8.dp)
                )
            }
            Text(
                text = "알림",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "알림 히스토리",
                    style = Headline2,
                    color = Black,
                )
                Text(
                    text = "모두 지우기",
                    style = Body1,
                    color = Gray600,
                    modifier = Modifier
                        .clickable {
                            if (!notificationList.isEmpty()) {
                                argument.intent(NotificationIntent.DeleteAllNotification)
                            }
                        }
                )
            }
            if (notificationList.isEmpty()) {
                Column(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "보여줄 알림이 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Gray900,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp)
                ) {
                    items(
                        count = notificationList.itemCount,
                        key = { index -> notificationList[index]?.notificationHistoryId ?: -1 }
                    ) { index ->
                        val notification = notificationList[index] ?: return@items
                        NotificationCard(
                            notification = notification,
                            onClicked = { deepLink ->
                                deepLink.takeIf { it.startsWith(DOMAIN) }
                                    ?.runCatching { toUri() }
                                    ?.getOrNull()
                                    ?.let { uri ->
                                        navController.navigate(uri)
                                    }
                            },
                            onDeleteIconClicked = { notificationId ->
                                argument.intent(
                                    NotificationIntent.DeleteNotificationById(
                                        notificationId
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        argument.intent(NotificationIntent.RefreshData)
        event.eventObserve { event ->

        }
    }
}

@Composable
private fun NotificationCard(
    notification: UserNotification,
    onClicked: (String) -> Unit,
    onDeleteIconClicked: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(6f)
                .clickable { onClicked(notification.deeplink) }
        ) {
            Text(
                text = notification.title,
                style = Headline2,
                color = Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = notification.description,
                style = Body1,
                color = Black
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            RippleBox(
                modifier = Modifier
                    .padding(4.dp),
                onClick = {
                    onDeleteIconClicked(notification.notificationHistoryId)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "Delete notification",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(horizontal = 8.dp),
                    tint = Gray900
                )
            }
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Gray900,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview
@Composable
private fun NotificationScreenPreview() {
    NotificationScreen(
        navController = rememberNavController(),
        argument = NotificationArgument(
            state = NotificationState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = NotificationData(
            notificationList = MutableStateFlow(
                PagingData.from(
                    listOf(
                        UserNotification(
                            notificationHistoryId = 1L,
                            title = "Welcome to the App!",
                            description = "Thank you for signing up! Explore and enjoy the app features.",
                            deeplink = "app://welcome"
                        ),
                        UserNotification(
                            notificationHistoryId = 2L,
                            title = "New Message",
                            description = "You have a new message from a friend. Check it out now!",
                            deeplink = "app://messages/2"
                        ),
                        UserNotification(
                            notificationHistoryId = 3L,
                            title = "Special Offer",
                            description = "Limited-time discount available just for you. Don't miss out!",
                            deeplink = "app://offers/special"
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
        )
    )
}
