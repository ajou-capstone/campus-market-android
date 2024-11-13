package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.BlueGray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat.ChatConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post.TradePostConstant

@Composable
fun TradeInfoScreen(
    navController: NavController,
    argument: TradeInfoArgument,
    data: TradeInfoData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val tradeInfo = data.tradeInfo
    val authorInfo = data.authorInfo
    val isOwnerOfThisTrade = (data.userInfo.id == authorInfo.id)

    var isDeleteConfirmButtonVisible by remember { mutableStateOf(false) }

    fun navigateToChatRoom(id: Long) {
        val route = makeRoute(
            ChatConstant.ROUTE,
            listOf(
                ChatConstant.ROUTE_ARGUMENT_ROOM_ID to id.toString()
            )
        )
        navController.navigate(route)
    }

    Scaffold(
        topBar = {
            TradeInfoTopBar(
                isOwnerOfThisTrade = isOwnerOfThisTrade,
                onNavigatePreviousScreenButton = { navController.safeNavigateUp() },
                onReportOptionClicked = { }, //TODO(신고 페이지로 이동)
                onDeleteArticleOptionClicked = {
                    isDeleteConfirmButtonVisible = true
                },
                onPatchArticleOptionClicked = {
                    val newRoute = makeRoute(
                        route = TradePostConstant.ROUTE,
                        arguments = mapOf(
                            TradePostConstant.ROUTE_ARGUMENT_ITEM_ID to tradeInfo.itemId.toString()
                        )
                    )
                    navController.safeNavigate(newRoute)
                }
            )
        },
        bottomBar = {
            TradeInfoBottomBar(
                isLiked = tradeInfo.isLiked,
                isSold = tradeInfo.isSold,
                price = tradeInfo.price,
                onLikeButtonClick = {
                    argument.intent(TradeInfoIntent.LikeButtonClicked)
                },
                onChatButtonClick = {
                    argument.intent(TradeInfoIntent.OnTradeStart)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(Indigo50)
        ) {
            TradeInfoImageViewer(
                thumbUrl = tradeInfo.thumbnail,
                imageUrls = tradeInfo.images
            )
            TradeInfoAuthor(
                authorInfo,
                authorNickname = tradeInfo.nickname
            )
            TradeInfoContent(
                title = tradeInfo.title,
                description = tradeInfo.description,
                category = tradeInfo.category,
                likeCount = tradeInfo.likeCount,
                chatCount = tradeInfo.chatCount
            )
        }

        if (isDeleteConfirmButtonVisible) {
            if (isOwnerOfThisTrade) {
                DialogScreen(
                    title = "게시글이 삭제되었습니다!",
                    isCancelable = true,
                    onConfirm = {
                        argument.intent(TradeInfoIntent.DeleteThisPost)
                        navController.safeNavigateUp()
                    },
                    onDismissRequest = {
                        isDeleteConfirmButtonVisible = false
                    }
                )
            } else {
                DialogScreen(
                    title = "삭제 권한이 없습니다",
                    isCancelable = false,
                    onDismissRequest = {
                        isDeleteConfirmButtonVisible = false
                    }
                )
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is TradeInfoEvent.NavigateToChatRoom -> {
                    navigateToChatRoom(id = event.id)
                }
            }
        }
    }
}

@Composable
private fun TradeInfoTopBar(
    isOwnerOfThisTrade: Boolean,
    onNavigatePreviousScreenButton: () -> Unit,
    onReportOptionClicked: () -> Unit,
    onPatchArticleOptionClicked: () -> Unit,
    onDeleteArticleOptionClicked: () -> Unit,
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .background(White)
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Navigate Up Button",
                tint = Black,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onNavigatePreviousScreenButton()
                    }
                    .padding(horizontal = 8.dp)
            )
            Text(
                text = "상세 정보",
                style = Headline2,
                color = Black
            )
        }
        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                tint = Black,
                contentDescription = "More Option Button",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        isDropDownExpanded = !isDropDownExpanded
                    }
                    .padding(horizontal = 8.dp)
            )
            val userOption = if (isOwnerOfThisTrade) {
                listOf("신고", "삭제", "수정")
            } else {
                listOf("신고")
            }

            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
            ) {
                userOption.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option)
                        },
                        onClick = {
                            isDropDownExpanded = false
                            when (option) {
                                "신고" -> {
                                    onReportOptionClicked()
                                }

                                "삭제" -> {
                                    onDeleteArticleOptionClicked()
                                }

                                "수정" -> {
                                    onPatchArticleOptionClicked()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TradeInfoImageViewer(thumbUrl: String, imageUrls: List<String>) {

    val pagerState = rememberPagerState(pageCount = { imageUrls.size + 1 })
    val thumbnailAndImage = listOf(thumbUrl) + imageUrls

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
    ) { page ->
        thumbnailAndImage.getOrNull(page).let {
            PostImage(
                data = thumbnailAndImage[page],
                modifier = Modifier.clip(shape = RoundedCornerShape(0))
            )
        }
    }
}

@Composable
private fun TradeInfoAuthor(
    authorInfo: UserProfile,
    authorNickname: String
) {
    Row(
        modifier = Modifier
            .border(2.dp, Color.Gray)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    //TODO(사용자 프로필 보여주는 화면으로 이동)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            PostImage(
                data = authorInfo.profileImage,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            )
            Text(
                text = authorNickname,
                style = Headline3,
                color = Black,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Text(text = "${authorInfo.rating} 점", color = Black, style = Headline3)
    }

}

@Composable
private fun TradeInfoContent(
    title: String,
    description: String,
    category: String,
    likeCount: Int,
    chatCount: Int
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val spacerPadding = 4.dp

        Text(text = title, color = Black, style = Headline2)
        Spacer(Modifier.padding(spacerPadding))

        Text(text = translateToKor(category), color = Black, style = Body1)
        Spacer(Modifier.padding(spacerPadding))

        Text(text = "$likeCount 명이 좋아함", color = Black, style = Body1)
        Spacer(Modifier.padding(spacerPadding))

        Text(text = "$chatCount 명이 관심 있어함", color = Black, style = Body1)
        Spacer(Modifier.padding(spacerPadding))

        HorizontalDivider(thickness = (0.4).dp, color = Black)

        Spacer(Modifier.padding(spacerPadding))
        Text(text = description, color = Black, style = Body1)
    }
}

@Composable
private fun TradeInfoBottomBar(
    isLiked: Boolean,
    price: Int,
    isSold: Boolean,
    onLikeButtonClick: () -> Unit,
    onChatButtonClick: () -> Unit,
) {

    val favIcon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .background(White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = favIcon,
                contentDescription = "Fav Icon",
                tint = Blue400,
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 8.dp)
                    .clickable {
                        onLikeButtonClick()
                    }
            )
            Text(text = "$price 원", color = Black, style = Headline3)
        }
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TradeItemStatus(isSold)
            Spacer(Modifier.padding(8.dp))
            TradeStartButton(isSold, onChatButtonClick)
        }
    }
}

@Composable
private fun TradeItemStatus(
    isSold: Boolean
) {
    if (isSold) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("거래 완료", modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
        }
    } else {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color.LightGray)
        ) {
            Text("거래중", modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
        }
    }
}

@Composable
private fun TradeStartButton(
    isSold: Boolean,
    onChatButtonClick: () -> Unit,
) {

    if (isSold) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(BlueGray200),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "거래 완료",
                modifier = Modifier.padding(8.dp),
                style = Headline2,
                color = Color.White
            )
        }
    } else {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Blue400)
                .clickable {
                    onChatButtonClick()
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "거래하기",
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 28.dp),
                style = Headline2,
                color = Color.White
            )
        }
    }
}

private fun translateToKor(engCategory: String): String {
    return when (engCategory) {
        "ELECTRONICS_IT" -> "전자기기/IT"
        "HOME_APPLIANCES" -> "가전제품"
        "FASHION_ACCESSORIES" -> "패션/액세서리"
        "ACCESSORIES" -> "액세서리"
        "BOOKS_EDUCATIONAL_MATERIALS" -> "서적/교육 자료"
        "STATIONERY_OFFICE_SUPPLIES" -> "문구/사무용품"
        "HOUSEHOLD_ITEMS" -> "생활용품"
        "KITCHEN_SUPPLIES" -> "주방용품"
        "FURNITURE_INTERIOR" -> "가구/인테리어"
        "SPORTS_LEISURE" -> "스포츠/레저"
        "ENTERTAINMENT_HOBBIES" -> "엔터테인먼트/취미"
        "OTHER" -> "기타"
        else -> "기타"
    }
}

@Preview
@Composable
private fun TradeInfoScreenPreview() {
    TradeInfoScreen(
        navController = rememberNavController(),
        argument = TradeInfoArgument(
            state = TradeInfoState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = TradeInfoData(
            authorInfo = UserProfile(
                id = 2001L,
                nickname = "SampleUser",
                profileImage = "https://www.google.com/url?sa=i&url=https%3A%2F%2Fnamu.wiki%2Fw%2F%25EB%25AA%25B0%253F%25EB%25A3%25A8&psig=AOvVaw0ZvXnvVvoooyjWzrpOLFTi&ust=1731290975363000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCOj6oM_X0IkDFQAAAAAdAAAAABAE",
                rating = 4.5
            ),
            tradeInfo = TradeInfo(
                campusId = 1L,
                itemId = 1001L,
                userId = 2001L,
                thumbnail = "https://item.kakaocdn.net/do/28a77143f3d25e892bda83e00c6f828a960f4ab09fe6e38bae8c63030c9b37f9",
                images = listOf(
                    "https://fastly.picsum.photos/id/63/250/250.jpg?hmac=hyyougRhLlxGL-aRzyFGfh6hW9_lWjkHMHlKodqU0ik",
                    "https://fastly.picsum.photos/id/63/250/250.jpg?hmac=hyyougRhLlxGL-aRzyFGfh6hW9_lWjkHMHlKodqU0ik"
                ),
                nickname = "SampleUser",
                title = "Example Item",
                category = "ELECTRONICS_IT",
                description = "대충 상품에 대한 설명입니다. 사장님이 맛있고 음식이 친절해요 어쩌고",
                chatCount = 5,
                likeCount = 20,
                price = 15000,
                isLiked = true,
                isSold = false
            ),
            userInfo = MyProfile.empty
        )
    )
}
