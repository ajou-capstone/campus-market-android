package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue300
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeArgument
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeIntent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeState
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant

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

    fun onItemClicked(tradeId: Long) {
        val tradeInfoRoute = makeRoute(
            route = TradeInfoConstant.ROUTE,
            arguments = mapOf(
                TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID to tradeId.toString()
            )
        )
        navController.navigate(tradeInfoRoute)
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
            MyRecentTradeListScreen(
                recentTradeList = visibleTradeList,
                onItemClicked = { itemId ->
                    onItemClicked(itemId)
                }
            )
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
private fun TradeHistoryCard(
    recentTrade: RecentTrade,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable {
                onClicked()
            }
    ) {
        PostImage(
            data = recentTrade.thumbnail,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = recentTrade.title,
                style = Headline2,
                color = Black,
            )
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = "${recentTrade.price} 원",
                style = Headline3,
                color = Black,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TradeItemStatus(recentTrade.isSold)
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Gray200,
        modifier = Modifier.padding(horizontal = 2.dp)
    )
}

@Composable
private fun TradeItemStatus(isSold: Boolean) {
    val backgroundColor = if (isSold) LightGray else Blue300
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

@Composable
private fun MyRecentTradeListScreen(
    recentTradeList: LazyPagingItems<RecentTrade>,
    onItemClicked: (Long) -> Unit
) {
    if (recentTradeList.isEmpty()) {
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

    LazyColumn {
        items(
            count = recentTradeList.itemCount,
            key = { index -> recentTradeList[index]?.id ?: -1 }
        ) { index ->
            val trade = recentTradeList[index] ?: return@items
            TradeHistoryCard(
                recentTrade = trade,
                onClicked = {
                    onItemClicked(trade.id)
                }
            )
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
            recentTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            id = 1L,
                            title = "Used Laptop",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false
                        ),
                        RecentTrade(
                            id = 2L,
                            title = "Antique Vase",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            recentBuyTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            id = 1L,
                            title = "Used Laptop",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false
                        ),
                        RecentTrade(
                            id = 2L,
                            title = "Antique Vase",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            recentSellTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            id = 1L,
                            title = "Used Laptop",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false
                        ),
                        RecentTrade(
                            id = 2L,
                            title = "Antique Vase",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}
