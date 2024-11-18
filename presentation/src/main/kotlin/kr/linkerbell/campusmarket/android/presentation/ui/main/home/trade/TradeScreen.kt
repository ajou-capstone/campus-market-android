package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space32
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post.TradePostConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.TradeSearchConstant

@Composable
fun TradeScreen(
    navController: NavController,
    viewModel: TradeViewModel = hiltViewModel(),
) {
    val argument: TradeScreenArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        TradeScreenArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: TradeData = Unit.let {
        val tradeList = viewModel.summarizedTradeList.collectAsLazyPagingItems()

        TradeData(
            summarizedTradeList = tradeList,
        )
    }

    ErrorObserver(viewModel)
    TradeScreen(
        navController = navController,
        argument = argument,
        data = data,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TradeScreen(
    navController: NavController,
    argument: TradeScreenArgument,
    data: TradeData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val refreshState = rememberPullToRefreshState()

    ConstraintLayout(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        val (topBar, contents, button) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            TradeSearchBar { navController.navigate(TradeSearchConstant.ROUTE) }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(refreshState.nestedScrollConnection)
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
            ) {
                items(
                    count = data.summarizedTradeList.itemCount,
                    key = { index -> data.summarizedTradeList[index]?.itemId ?: -1 }
                ) { index ->
                    val trade = data.summarizedTradeList[index] ?: return@items
                    TradeItemCard(
                        item = trade,
                        onItemCardClicked = {
                            val tradeInfoRoute = makeRoute(
                                route = TradeInfoConstant.ROUTE,
                                arguments = mapOf(
                                    TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID to trade.itemId.toString()
                                )
                            )
                            navController.navigate(tradeInfoRoute)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (refreshState.isRefreshing) {
                argument.intent(TradeScreenIntent.RefreshNewTrades)
                refreshState.endRefresh()
            }
            if (refreshState.progress > 0 || refreshState.isRefreshing) {
                PullToRefreshContainer(
                    state = refreshState,
                    containerColor = White,
                    contentColor = Blue200,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(Space20)
                .constrainAs(button) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            FloatingActionButton(
                modifier = Modifier.size(Space56),
                shape = CircleShape,
                containerColor = Blue400,
                onClick = {
                    val postRoute = makeRoute(
                        route = TradePostConstant.ROUTE,
                        arguments = mapOf(
                            TradePostConstant.ROUTE_ARGUMENT_ITEM_ID to "-1"
                        )
                    )
                    navController.navigate(postRoute)
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space32),
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    tint = White
                )
            }
        }
    }

    LaunchedEffectWithLifecycle(coroutineContext) {
        argument.intent(TradeScreenIntent.RefreshNewTrades)
    }
}

@Composable
private fun TradeItemCard(
    item: SummarizedTrade,
    onItemCardClicked: (Long) -> Unit
) {
    Box(
        Modifier
            .shadow(4.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable {
                onItemCardClicked(item.itemId)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
        ) {
            PostImage(
                data = item.thumbnail,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Text(
                    text = item.title,
                    style = Headline3,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 4.dp),
                )

                Text(
                    text = item.nickname,
                    style = Caption2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "${item.chatCount} 명이 대화중",
                    style = Caption2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(min = 100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TradeItemStatus(
                            isSold = item.itemStatus == "SOLDOUT"
                        )
                        Text(
                            text = "${item.price} 원",
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val favIcon =
                            if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                        Icon(
                            imageVector = favIcon,
                            tint = Gray900,
                            contentDescription = "isLike",
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(item.likeCount.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun TradeSearchBar(
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(Space56)
            .background(White)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TypingTextField(
            text = "",
            onValueChange = { },
            hintText = "검색어를 입력하세요",
            modifier = Modifier
                .padding(horizontal = Space20)
                .weight(1f),
            onTextFieldFocusChange = { isFocused ->
                if (isFocused) {
                    onClicked()
                }
            }
        )

        RippleBox(
            modifier = Modifier.padding(end = Space20),
            onClick = {
//                navigateToNotificationScreen()
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
}

@Composable
private fun TradeItemStatus(isSold: Boolean) {
    val backgroundColor = if (isSold) LightGray else Blue100
    val text = if (isSold) "거래 완료" else "거래 가능"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Body1,
            color = White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Preview
@Composable
private fun TradeScreenPreview() {
    TradeScreen(
        navController = rememberNavController(),
        argument = TradeScreenArgument(
            state = TradeScreenState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = TradeData(
            summarizedTradeList = MutableStateFlow(
                PagingData.from(
                    listOf(
                        SummarizedTrade(
                            itemId = 1L,
                            userId = 1L,
                            nickname = "장성혁",
                            thumbnail = "https://picsum.photos/200",
                            title = "콜라 팝니다",
                            price = 1000,
                            chatCount = 5,
                            likeCount = 2,
                            itemStatus = "",
                            isLiked = true
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}
