package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space12
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space4
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space40
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ComposableLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat.ChatConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification.NotificationConstant

@Composable
fun ChatRoomScreen(
    navController: NavController,
    viewModel: ChatRoomViewModel = hiltViewModel()
) {
    val argument: ChatRoomArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        ChatRoomArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: ChatRoomData = Unit.let {
        val roomList by viewModel.roomList.collectAsStateWithLifecycle()
        val messageList by viewModel.messageList.collectAsStateWithLifecycle()

        ChatRoomData(
            roomList = roomList,
            messageList = messageList
        )
    }

    ErrorObserver(viewModel)
    ChatRoomScreen(
        navController = navController,
        argument = argument,
        data = data
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatRoomScreen(
    navController: NavController,
    argument: ChatRoomArgument,
    data: ChatRoomData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var showingRoomMenu: Room? by remember { mutableStateOf(null) }

    fun navigateToChatScreen(
        id: Long
    ) {
        navController.safeNavigate("${ChatConstant.ROUTE}/$id")
    }

    fun navigateToNotificationScreen() {
        navController.safeNavigate(NotificationConstant.ROUTE)
    }

    if (showingRoomMenu != null) {
        val isAlarm = showingRoomMenu?.isAlarm ?: return
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = {
                showingRoomMenu = null
            }
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = Space12),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAlarm) "알람 끄기" else "알람 켜기",
                        style = Body1.merge(Gray900),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Space20, vertical = Space8)
                            .clickable {
                                intent(
                                    ChatRoomIntent.SetRoomNotification(
                                        id = showingRoomMenu?.id ?: -1L,
                                        isNotification = !isAlarm
                                    )
                                )
                                showingRoomMenu = null
                            }
                    )

                    Text(
                        text = "채팅방 나가기",
                        style = Body1.merge(Gray900),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Space20, vertical = Space8)
                            .clickable {
                                intent(
                                    ChatRoomIntent.QuitRoom(
                                        id = showingRoomMenu?.id ?: -1L
                                    )
                                )
                                showingRoomMenu = null
                            }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .height(Space56)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "채팅",
                style = Headline2.merge(Gray900),
                modifier = Modifier
                    .padding(start = Space20)
                    .weight(1f)
            )
            RippleBox(
                modifier = Modifier.padding(end = Space20),
                onClick = {
                    navigateToNotificationScreen()
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(Space24),
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = null,
                    tint = Gray900
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(
                items = data.roomList,
                key = { room -> room.id }
            ) { room ->
                val latestMessage = data.messageList.maxByOrNull { message ->
                    when (message) {
                        is Message.Text -> {
                            if (message.chatRoomId == room.id) {
                                message.createdAt
                            } else {
                                -1L
                            }
                        }

                        is Message.Image -> {
                            if (message.chatRoomId == room.id) {
                                message.createdAt
                            } else {
                                -1L
                            }
                        }

                        is Message.Schedule -> {
                            if (message.chatRoomId == room.id) {
                                message.createdAt
                            } else {
                                -1L
                            }
                        }
                    }
                }?.let { message ->
                    when (message) {
                        is Message.Text -> {
                            if (message.chatRoomId == room.id) {
                                message.content
                            } else {
                                ""
                            }
                        }

                        is Message.Image -> {
                            if (message.chatRoomId == room.id) {
                                "(사진)"
                            } else {
                                ""
                            }
                        }

                        is Message.Schedule -> {
                            if (message.chatRoomId == room.id) {
                                "(스케줄)"
                            } else {
                                ""
                            }
                        }
                    }
                } ?: ""

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                navigateToChatScreen(
                                    id = room.id
                                )
                            },
                            onLongClick = {
                                showingRoomMenu = room
                            },
                            onLongClickLabel = null
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PostImage(
                        data = room.thumbnail,
                        modifier = Modifier
                            .padding(Space20)
                            .size(Space40)
                    )
                    Column(
                        modifier = Modifier
                            .padding(end = Space20)
                            .weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = room.title,
                                style = Body1.merge(Gray900)
                            )
                            Spacer(modifier = Modifier.width(Space4))
                            if (!room.isAlarm) {
                                Icon(
                                    modifier = Modifier.size(Space12),
                                    painter = painterResource(R.drawable.ic_notification_off),
                                    contentDescription = null,
                                    tint = Gray400
                                )
                            }
                        }
                        Text(
                            text = latestMessage,
                            style = Body2.merge(Gray900)
                        )
                    }
                }
            }
        }
    }

    ComposableLifecycle { lifecycleOwner, lifecycleEvent ->
        when (lifecycleEvent) {
            Lifecycle.Event.ON_START -> {
                intent(ChatRoomIntent.Refresh)
            }

            Lifecycle.Event.ON_STOP -> {
                intent(ChatRoomIntent.Session.Disconnect)
            }

            else -> Unit
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Preview
@Composable
private fun ChatRoomScreenPreview() {
    val currentTime = System.currentTimeMillis()
    ChatRoomScreen(
        navController = rememberNavController(),
        argument = ChatRoomArgument(
            state = ChatRoomState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = ChatRoomData(
            roomList = listOf(
                Room(
                    id = 1L,
                    userId = 1L,
                    tradeId = 1L,
                    title = "title1",
                    isAlarm = true,
                    readLatestMessageId = 1L,
                    thumbnail = "https://placehold.co/600x400"
                ),
                Room(
                    id = 2L,
                    userId = 2L,
                    tradeId = 2L,
                    title = "title2",
                    isAlarm = false,
                    readLatestMessageId = 2L,
                    thumbnail = "https://placehold.co/600x400"
                )
            ),
            messageList = listOf(
                Message.Text(
                    id = 1L,
                    chatRoomId = 1L,
                    userId = 1L,
                    content = "안녕하세요.",
                    createdAt = currentTime - 5L
                ),
                Message.Image(
                    id = 2L,
                    chatRoomId = 1L,
                    userId = 1L,
                    content = "https://placehold.co/600x400",
                    createdAt = currentTime - 4L
                ),
                Message.Text(
                    id = 3L,
                    chatRoomId = 1L,
                    userId = 1L,
                    content = "해당 물품 구매 희망합니다.",
                    createdAt = currentTime - 3L
                ),
                Message.Text(
                    id = 4L,
                    chatRoomId = 1L,
                    userId = 2L,
                    content = "이거 말씀하시는 건가요?",
                    createdAt = currentTime - 2L
                ),
                Message.Image(
                    id = 5L,
                    chatRoomId = 1L,
                    userId = 2L,
                    content = "https://placehold.co/600x400",
                    createdAt = currentTime - 1L
                ),
                Message.Text(
                    id = 6L,
                    chatRoomId = 1L,
                    userId = 2L,
                    content = "엄청나게 긴 메세지를 엄청나게 보내는데 ".repeat(20),
                    createdAt = currentTime
                ),
            )
        )
    )
}
