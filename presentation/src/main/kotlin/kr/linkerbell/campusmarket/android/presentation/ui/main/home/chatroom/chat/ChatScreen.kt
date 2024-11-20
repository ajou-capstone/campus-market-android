package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Green400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space12
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space32
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space4
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space40
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space52
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space60
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space80
import kr.linkerbell.campusmarket.android.presentation.common.theme.Teal400
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ComposableLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery.GalleryScreen
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare.ScheduleCompareConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating.RatingConstant

@Composable
fun ChatScreen(
    navController: NavController,
    argument: ChatArgument,
    data: ChatData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext
    val localConfiguration = LocalConfiguration.current
    val scrollState = rememberLazyListState()

    var message: String by remember { mutableStateOf("") }

    var isMessageMenuOpen: Boolean by remember { mutableStateOf(false) }
    var isGalleryShowing by remember { mutableStateOf(false) }
    var isSellCompleteDialogShowing by remember { mutableStateOf(false) }

    fun navigateToUserProfile(id: Long) {

    }

    fun navigateToScheduleCompare(id: Long) {
        val route = makeRoute(
            ScheduleCompareConstant.ROUTE,
            listOf(ScheduleCompareConstant.ROUTE_ARGUMENT_USER_ID to id)
        )
        navController.safeNavigate(route)
    }

    if (isGalleryShowing) {
        GalleryScreen(
            navController = navController,
            onDismissRequest = { isGalleryShowing = false },
            onResult = {
                val image = it.getOrNull(0) ?: return@GalleryScreen
                intent(ChatIntent.Session.SendImage(image))
                isGalleryShowing = false
            }
        )
    }

    if (isSellCompleteDialogShowing) {
        DialogScreen(
            title = "거래 완료",
            message = "상대방과의 거래를 완료했습니다.",
            isCancelable = false,
            onConfirm = {
                navController.safeNavigateUp()

                val newRoute = makeRoute(
                    route = RatingConstant.ROUTE,
                    arguments = mapOf(
                        RatingConstant.ROUTE_ARGUMENT_USER_ID to data.userProfile.id,
                        RatingConstant.ROUTE_ARGUMENT_ITEM_ID to data.trade.itemId
                    )
                )
                navController.popBackStack()
                navController.navigate(newRoute)
            },
            onDismissRequest = { isSellCompleteDialogShowing = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(Blue50)
    ) {
        Row(
            modifier = Modifier
                .height(Space56)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RippleBox(
                modifier = Modifier.padding(start = Space20),
                onClick = {
                    navController.safeNavigateUp()
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space24),
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = null,
                    tint = Gray900
                )
            }
            Text(
                text = data.room.title,
                style = Headline2.merge(Gray900),
                modifier = Modifier
                    .padding(horizontal = Space20)
                    .weight(1f)
            )
            if (data.trade.userId == data.myProfile.id && !data.trade.isSold) {
                RippleBox(
                    modifier = Modifier.padding(end = Space20),
                    onClick = {
                        intent(ChatIntent.OnSell)
                    }
                ) {
                    Text(
                        text = "판매하기",
                        style = Body0.merge(Gray900)
                    )
                }
            } else {
                Text(
                    text = "거래완료",
                    modifier = Modifier.padding(end = Space20),
                    style = Body0.merge(Gray400)
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(Space8),
            contentPadding = PaddingValues(vertical = Space20)
        ) {
            items(
                items = data.messageList,
                key = { message ->
                    when (message) {
                        is Message.Image -> message.id
                        is Message.Text -> message.id
                        is Message.Schedule -> message.id
                    }
                },
                contentType = { message ->
                    when {
                        message is Message.Image && message.userId != data.userProfile.id -> {
                            0L
                        }

                        message is Message.Image && message.userId == data.userProfile.id -> {
                            1L
                        }

                        message is Message.Text && message.userId != data.userProfile.id -> {
                            2L
                        }

                        message is Message.Text && message.userId == data.userProfile.id -> {
                            3L
                        }

                        message is Message.Schedule && message.userId != data.userProfile.id -> {
                            4L
                        }

                        message is Message.Schedule && message.userId == data.userProfile.id -> {
                            5L
                        }

                        else -> {
                            -1L
                        }
                    }
                }
            ) { message ->
                when {
                    message is Message.Image && message.userId != data.userProfile.id -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Space20),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier.background(
                                    color = Blue200,
                                    shape = RoundedCornerShape(Space20, Space4, Space20, Space20)
                                )
                            ) {
                                PostImage(
                                    data = message.content,
                                    modifier = Modifier
                                        .width(localConfiguration.screenWidthDp.dp * 3 / 5)
                                        .padding(Space12)
                                )
                            }
                        }
                    }

                    message is Message.Image && message.userId == data.userProfile.id -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Space20),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            PostImage(
                                data = data.userProfile.profileImage,
                                modifier = Modifier
                                    .size(Space40)
                                    .clickable {
                                        navigateToUserProfile(
                                            id = data.userProfile.id
                                        )
                                    }
                            )
                            Spacer(modifier = Modifier.width(Space8))
                            Box(
                                modifier = Modifier.background(
                                    color = Blue200,
                                    shape = RoundedCornerShape(Space4, Space20, Space20, Space20)
                                )
                            ) {
                                PostImage(
                                    data = message.content,
                                    modifier = Modifier
                                        .width(localConfiguration.screenWidthDp.dp * 3 / 5)
                                        .padding(Space12)
                                )
                            }
                        }
                    }

                    message is Message.Text && message.userId != data.userProfile.id -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Space20),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier.background(
                                    color = Blue200,
                                    shape = RoundedCornerShape(Space20, Space4, Space20, Space20)
                                )
                            ) {
                                Text(
                                    text = message.content,
                                    style = Body2.merge(Gray900),
                                    modifier = Modifier
                                        .padding(Space12)
                                        .widthIn(max = localConfiguration.screenWidthDp.dp * 3 / 5)
                                )
                            }
                        }
                    }

                    message is Message.Text && message.userId == data.userProfile.id -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Space20),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            PostImage(
                                data = data.userProfile.profileImage,
                                modifier = Modifier
                                    .size(Space40)
                                    .clickable {
                                        navigateToUserProfile(
                                            id = data.userProfile.id
                                        )
                                    }
                            )
                            Spacer(modifier = Modifier.width(Space8))
                            Box(
                                modifier = Modifier.background(
                                    color = Blue200,
                                    shape = RoundedCornerShape(Space4, Space20, Space20, Space20)
                                )
                            ) {
                                Text(
                                    text = message.content,
                                    style = Body2.merge(Gray900),
                                    modifier = Modifier
                                        .padding(Space12)
                                        .widthIn(max = localConfiguration.screenWidthDp.dp * 3 / 5)
                                )
                            }
                        }
                    }

                    message is Message.Schedule && message.userId != data.userProfile.id -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Space20),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier.background(
                                    color = Blue100,
                                    shape = RoundedCornerShape(Space12, Space12, Space12, Space12)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.clickable {
                                        navigateToScheduleCompare(
                                            id = data.userProfile.id
                                        )
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "시간표 확인하기",
                                        style = Body1.merge(Gray900),
                                        modifier = Modifier
                                            .padding(Space12)
                                            .widthIn(max = localConfiguration.screenWidthDp.dp * 3 / 5)
                                    )
                                    Icon(
                                        modifier = Modifier.size(Space24),
                                        painter = painterResource(R.drawable.ic_chevron_right),
                                        contentDescription = null,
                                        tint = Gray900
                                    )
                                    Spacer(modifier = Modifier.width(Space12))
                                }
                            }
                        }
                    }

                    message is Message.Schedule && message.userId == data.userProfile.id -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Space20),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            PostImage(
                                data = data.userProfile.profileImage,
                                modifier = Modifier
                                    .size(Space40)
                                    .clickable {
                                        navigateToUserProfile(
                                            id = data.userProfile.id
                                        )
                                    }
                            )
                            Spacer(modifier = Modifier.width(Space8))
                            Box(
                                modifier = Modifier.background(
                                    color = Blue100,
                                    shape = RoundedCornerShape(Space12, Space12, Space12, Space12)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.clickable {
                                        navigateToScheduleCompare(
                                            id = data.userProfile.id
                                        )
                                    },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "시간표 확인하기",
                                        style = Body1.merge(Gray900),
                                        modifier = Modifier
                                            .padding(Space12)
                                            .widthIn(max = localConfiguration.screenWidthDp.dp * 3 / 5)
                                    )
                                    Icon(
                                        modifier = Modifier.size(Space24),
                                        painter = painterResource(R.drawable.ic_chevron_right),
                                        contentDescription = null,
                                        tint = Gray900
                                    )
                                    Spacer(modifier = Modifier.width(Space12))
                                }
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .heightIn(min = Space56)
                .background(White)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(Space20))
                if (isMessageMenuOpen) {
                    RippleBox(
                        onClick = {
                            isMessageMenuOpen = false
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(Space24),
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = Gray900
                        )
                    }
                } else {
                    RippleBox(
                        onClick = {
                            isMessageMenuOpen = true
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(Space24),
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = null,
                            tint = Gray900
                        )
                    }
                }
                Spacer(modifier = Modifier.width(Space20))
                TypingTextField(
                    text = message,
                    modifier = Modifier
                        .heightIn(max = Space80)
                        .padding(vertical = Space12)
                        .weight(1f),
                    onValueChange = { message = it },
                    maxLines = Int.MAX_VALUE,
                    maxTextLength = 1000
                )
                Spacer(modifier = Modifier.width(Space20))
                if (message.isEmpty()) {
                    Icon(
                        modifier = Modifier.size(Space24),
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = null,
                        tint = Gray400
                    )
                } else {
                    RippleBox(
                        onClick = {
                            intent(ChatIntent.Session.SendText(message))
                            message = ""
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(Space24),
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = null,
                            tint = Gray900
                        )
                    }
                }
                Spacer(modifier = Modifier.width(Space20))
            }
            if (isMessageMenuOpen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Space20)
                ) {
                    Spacer(modifier = Modifier.width(Space20))
                    Column {
                        Spacer(modifier = Modifier.height(Space20))
                        RippleBox(
                            onClick = {
                                isGalleryShowing = true
                                isMessageMenuOpen = false
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Space52)
                                        .clip(CircleShape)
                                        .background(Green400, CircleShape),
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(Space32)
                                            .align(Alignment.Center),
                                        painter = painterResource(R.drawable.ic_image),
                                        contentDescription = null,
                                        tint = White
                                    )
                                }
                                Spacer(modifier = Modifier.height(Space8))
                                Text(
                                    text = "사진",
                                    style = Body1.merge(Gray900)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Space60))
                    }
                    Column {
                        Spacer(modifier = Modifier.height(Space20))
                        RippleBox(
                            onClick = {
                                intent(ChatIntent.Session.SendSchedule)
                                isMessageMenuOpen = false
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Space52)
                                        .clip(CircleShape)
                                        .background(Teal400, CircleShape),
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(Space32)
                                            .align(Alignment.Center),
                                        painter = painterResource(R.drawable.ic_calendar),
                                        contentDescription = null,
                                        tint = White
                                    )
                                }
                                Spacer(modifier = Modifier.height(Space8))
                                Text(
                                    text = "시간표",
                                    style = Body1.merge(Gray900)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(Space60))
                    }
                }
            }
        }
    }

    ComposableLifecycle { lifecycleOwner, lifecycleEvent ->
        when (lifecycleEvent) {
            Lifecycle.Event.ON_START -> {
                intent(ChatIntent.Refresh)
            }

            Lifecycle.Event.ON_STOP -> {
                intent(ChatIntent.Session.Disconnect)
            }

            else -> Unit
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is ChatEvent.Sell.Success -> {
                    isSellCompleteDialogShowing = true
                }
            }
        }
    }

    LaunchedEffect(data.messageList) {
        val lastIndex = data.messageList.lastIndex.takeIf { it > -1 } ?: return@LaunchedEffect
        scrollState.scrollToItem(lastIndex)
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    val currentTime = System.currentTimeMillis()
    ChatScreen(
        navController = rememberNavController(),
        argument = ChatArgument(
            state = ChatState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = ChatData(
            messageList = listOf(
                Message.Schedule(
                    id = 0L,
                    chatRoomId = 1L,
                    userId = 1L,
                    createdAt = currentTime - 6L
                ),
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
                    content = "엄청나게 긴 메세지를 엄청나게 보내는데 ".repeat(100),
                    createdAt = currentTime
                ),
            ),
            userProfile = UserProfile(
                id = 1L,
                nickname = "정은재",
                profileImage = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50",
                rating = 4.5
            ),
            myProfile = MyProfile(
                id = 2L,
                campusId = 1L,
                loginEmail = "lorenzo.ballard@example.com",
                schoolEmail = "selena.weaver@example.com",
                nickname = "장성혁",
                profileImage = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50",
                rating = 4.5
            ),
            room = Room(
                id = 1L,
                userId = 1L,
                tradeId = 1L,
                title = "title1",
                isAlarm = true,
                readLatestMessageId = 1L
            ),
            trade = TradeInfo(
                itemId = 1L,
                userId = 1L,
                campusId = 1L,
                nickname = "정은재",
                title = "title1",
                description = "description1",
                price = 10000,
                category = "category1",
                thumbnail = "https://placehold.co/600x400",
                images = listOf("https://placehold.co/600x400"),
                chatCount = 1,
                likeCount = 1,
                isLiked = true,
                isSold = false
            )
        )
    )
}
