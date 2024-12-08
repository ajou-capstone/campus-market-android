package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kotlinx.datetime.LocalDateTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeArgument
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeEvent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeIntent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeState
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common.StarRatingBar
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common.TradeHistoryCard
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant
import timber.log.Timber

@Composable
fun MyRecentTradeScreen(
    navController: NavController,
    argument: MyRecentTradeArgument,
    data: MyRecentTradeData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val recentTradeList by remember { mutableStateOf(data.recentTrades) }
    val recentBuyTradeList by remember { mutableStateOf(data.recentBuyTrades) }
    val recentSalesTradeList by remember { mutableStateOf(data.recentSellTrades) }

    var selectedTab by remember { mutableIntStateOf(0) }

    var isReviewDialogVisible by remember { mutableStateOf(false) }
    var isReviewRequested by remember { mutableStateOf(false) }
    var isReviewSuccessDialogVisible by remember { mutableStateOf(false) }

    var targetUserId by remember { mutableLongStateOf(-1L) }
    var itemId by remember { mutableLongStateOf(-1L) }

    if (isReviewDialogVisible) {
        ReviewDialog(
            isReviewRequested = isReviewRequested,
            onReviewButtonClicked = { userDescription, userRating ->
                isReviewRequested = true
                argument.intent(
                    MyRecentTradeIntent.RateUser(
                        targetUserId,
                        itemId,
                        userDescription,
                        userRating
                    )
                )
            },
            onDismissRequest = {
                isReviewDialogVisible = false
            }
        )
    }

    if (isReviewSuccessDialogVisible) {
        DialogScreen(
            title = "리뷰가 등록되었습니다!",
            isCancelable = false,
            onConfirm = { },
            onDismissRequest = {
                isReviewSuccessDialogVisible = false
            }
        )
    }

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
                text = "최근 거래 목록",
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
            Row {
                MyRecentTradeListTab(
                    text = "전체",
                    isSelected = (selectedTab == 0),
                    onClicked = {
                        selectedTab = 0
                        argument.intent(MyRecentTradeIntent.RefreshAllTradeList)
                    },
                    modifier = Modifier.weight(1f)
                )
                MyRecentTradeListTab(
                    text = "판매",
                    isSelected = (selectedTab == 1),
                    onClicked = {
                        selectedTab = 1
                        argument.intent(MyRecentTradeIntent.RefreshSellTradeList)
                    },
                    modifier = Modifier.weight(1f)
                )
                MyRecentTradeListTab(
                    text = "구매",
                    isSelected = (selectedTab == 2),
                    onClicked = {
                        selectedTab = 2
                        argument.intent(MyRecentTradeIntent.RefreshBuyTradeList)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            val visibleTradeList = when (selectedTab) {
                0 -> recentTradeList
                1 -> recentSalesTradeList
                2 -> recentBuyTradeList
                else -> recentTradeList
            }
            if (visibleTradeList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "보여줄 항목이 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    count = visibleTradeList.itemCount,
                    key = { index -> visibleTradeList[index]?.itemId ?: -1 }
                ) { index ->
                    val trade = visibleTradeList[index] ?: return@items
                    TradeHistoryCard(
                        isOwnerOfThisTrade = (trade.userId == data.myId),
                        recentTrade = trade,
                        onClicked = {
                            val tradeInfoRoute = makeRoute(
                                route = TradeInfoConstant.ROUTE,
                                arguments = mapOf(
                                    TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID
                                            to trade.itemId.toString()
                                )
                            )
                            navController.safeNavigate(tradeInfoRoute)
                        },
                        onAddReviewClicked = {
                            targetUserId = if (trade.userId == data.myId) trade.buyerId
                                            else trade.userId
                            itemId = trade.itemId
                            Timber.tag("siri22").d("targetUserId = ${targetUserId}, buyer = ${trade.buyerId}, myId = ${data.myId}")
                            isReviewDialogVisible = true
                        }
                    )
                }
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is MyRecentTradeEvent.RateSuccess -> {
                    isReviewSuccessDialogVisible = true
                    isReviewDialogVisible = false
                    argument.intent(MyRecentTradeIntent.RefreshTradeList)
                }

                is MyRecentTradeEvent.RateFail -> {
                    isReviewRequested = false
                }
            }
        }
    }
}

@Composable
private fun MyRecentTradeListTab(
    text: String,
    isSelected: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier
) {
    val textStyle = if (isSelected) Headline1 else Headline2

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClicked()
            }
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (isSelected) {
            HorizontalDivider(
                thickness = 2.dp,
                color = Gray900,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun ReviewDialog(
    onReviewButtonClicked: (String, Int) -> Unit,
    onDismissRequest: () -> Unit,
    isReviewRequested: Boolean
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var userRating by remember { mutableIntStateOf(5) }
    var userDescription by remember { mutableStateOf("") }

    var descriptionLength by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .size(height = screenHeight * 0.6f, width = screenWidth * 0.8f)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "거래는 어떠셨나요?",
                    style = Headline2,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = "상대방에 대한 리뷰를 남겨주세요",
                    style = Body2,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = userRating.toString(),
                    style = Headline1,
                    modifier = Modifier.padding(8.dp)
                )
                StarRatingBar(
                    rating = userRating,
                    180.dp,
                    onRatingChanged = { newRating ->
                        userRating = newRating
                    }
                )

                TypingTextField(
                    text = userDescription,
                    onValueChange = {
                        if (descriptionLength <= 200) {
                            userDescription = it
                            descriptionLength = userDescription.length
                        }
                    },
                    maxLines = 100,
                    modifier = Modifier
                        .heightIn(min = 140.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Text(
                    text = if (descriptionLength <= 200) "(${descriptionLength}/200)"
                    else "리뷰는 최대 200자까지만 작성할 수 있어요!",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp),
                    style = Caption1,
                    color = if (descriptionLength <= 200) Gray900 else Red400
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.wrapContentSize()) {
                    ConfirmButton(
                        properties = ConfirmButtonProperties(
                            size = ConfirmButtonSize.Large,
                            type = ConfirmButtonType.Secondary
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onDismissRequest()
                        }
                    ) { style ->
                        Text(
                            text = "나중에 할게요",
                            style = style
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    ConfirmButton(
                        properties = ConfirmButtonProperties(
                            size = ConfirmButtonSize.Large,
                            type = ConfirmButtonType.Primary
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (!isReviewRequested) {
                                onReviewButtonClicked(userDescription, userRating)
                            }
                        }
                    ) { style ->
                        Text(
                            text = "평가하기",
                            style = style
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecentTradeScreenPreview() {
    MyRecentTradeScreen(
        navController = rememberNavController(),
        argument = MyRecentTradeArgument(
            state = MyRecentTradeState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = MyRecentTradeData(
            myId = 0L,
            recentTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            itemId = 1L,
                            title = "Used Laptop",
                            userId = 0L,
                            buyerId = 1L,
                            nickname = "author_1",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        ),
                        RecentTrade(
                            itemId = 2L,
                            title = "Antique Vase",
                            userId = 1L,
                            buyerId = 2L,
                            nickname = "author_2",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            recentBuyTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            itemId = 1L,
                            title = "Used Laptop",
                            userId = 0L,
                            buyerId = 3L,
                            nickname = "author_1",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        ),
                        RecentTrade(
                            itemId = 2L,
                            title = "Antique Vase",
                            userId = 1L,
                            buyerId = 1L,
                            nickname = "author_2",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            recentSellTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            itemId = 1L,
                            title = "Used Laptop",
                            userId = 0L,
                            buyerId = 1L,
                            nickname = "author_1",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        ),
                        RecentTrade(
                            itemId = 2L,
                            title = "Antique Vase",
                            userId = 1L,
                            buyerId = 1L,
                            nickname = "author_2",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}
