package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.likes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.common.SummarizedTradeCard
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant

@Composable
fun MyLikesScreen(
    navController: NavController,
    argument: MyLikesArgument,
    data: MyLikesData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val myLikes by remember { mutableStateOf(data.myLikes) }

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
                text = "찜한 목록",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            if (myLikes.isEmpty()) {
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
                    count = myLikes.itemCount,
                    key = { index -> myLikes[index]?.itemId ?: -1 }
                ) { index ->
                    val trade = myLikes[index] ?: return@items
                    SummarizedTradeCard(
                        item = trade,
                        onItemCardClicked = {
                            onItemClicked(trade.itemId)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyLikesScreenPreview() {
    MyLikesScreen(
        navController = rememberNavController(),
        argument = MyLikesArgument(
            state = MyLikesState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = MyLikesData(
            myLikes = MutableStateFlow(
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
