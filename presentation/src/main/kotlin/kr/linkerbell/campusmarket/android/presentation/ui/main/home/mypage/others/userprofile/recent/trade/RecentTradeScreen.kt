package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common.TradeHistoryCard
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.rating.RatingConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant

@Composable
fun RecentTradeScreen(
    navController: NavController,
    argument: RecentTradeArgument,
    data: RecentTradeData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val recentTradeList = data.recentTrades

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
                text = "최근 판매",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
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
            if (recentTradeList.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 판매중인 물건이 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(
                    count = recentTradeList.itemCount,
                    key = { index -> recentTradeList[index]?.itemId ?: -1 }
                ) { index ->
                    val trade = recentTradeList[index] ?: return@items
                    TradeHistoryCard(
                        isAddReviewIconVisible = false,
                        isOwnerOfThisTrade = (data.userId == trade.userId),
                        recentTrade = trade,
                        onClicked = {
                            val tradeInfoRoute = makeRoute(
                                route = TradeInfoConstant.ROUTE,
                                arguments = mapOf(
                                    TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID to trade.itemId.toString()
                                )
                            )
                            navController.navigate(tradeInfoRoute)
                        },
                        onAddReviewClicked = { userId, itemId ->
                            val reviewRoute = makeRoute(
                                route = RatingConstant.ROUTE,
                                arguments = mapOf(
                                    RatingConstant.ROUTE_ARGUMENT_USER_ID to userId,
                                    RatingConstant.ROUTE_ARGUMENT_ITEM_ID to itemId
                                )
                            )
                            navController.safeNavigate(reviewRoute)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecentTradeScreenPreview() {
    RecentTradeScreen(
        navController = rememberNavController(),
        argument = RecentTradeArgument(
            state = RecentTradeState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = RecentTradeData(
            recentTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            itemId = 1L,
                            title = "Used Laptop",
                            userId = 0L,
                            nickname = "author_1",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = false
                        ),
                        RecentTrade(
                            itemId = 2L,
                            title = "Antique Vase",
                            userId = 2L,
                            nickname = "author_2",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 2, 0, 0, 0),
                            isReviewed = false
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            userId = 2L
        )
    )
}
