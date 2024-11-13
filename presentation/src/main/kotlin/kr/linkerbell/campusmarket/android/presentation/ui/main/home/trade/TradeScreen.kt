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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
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

@Composable
private fun TradeScreen(
    navController: NavController,
    argument: TradeScreenArgument,
    data: TradeData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        TradeSearchBar {
            navController.navigate(TradeSearchConstant.ROUTE)
        }
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
        ) {
            itemsIndexed(
                items = data.summarizedTradeList.itemSnapshotList,
                key = { _, item -> item?.itemId ?: -1 }
            ) { _, trade ->
                trade?.let {
                    TradeItemCard(
                        item = it,
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
        }
    }

    Box(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        TradeScreenPostButton(
            onClick = {
                val postRoute = makeRoute(
                    route = TradePostConstant.ROUTE,
                    arguments = mapOf(
                        TradePostConstant.ROUTE_ARGUMENT_ITEM_ID to "-1"
                    )
                )
                navController.navigate(postRoute)
            }
        )
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
                modifier = Modifier.size(85.dp)
            )
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = item.title,
                            style = Headline3,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f),
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        TradeItemStatus(isSold = item.itemStatus === "FORSALE")
                    }
                    Text("${item.price} 원", modifier = Modifier.padding(start = 8.dp))
                }
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Home",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(item.likeCount.toString())
                }
            }
        }
    }
}

@Composable
private fun TradeSearchBar(
    navigateToTradeSearchScreen: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            //Banner
            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(36.dp)
                    .background(Color.Gray)
                    .weight(3f)
            ) {
                Text("Logo Here")
            }

            Spacer(Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .weight(8f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Gray50)
                    .clickable {
                        navigateToTradeSearchScreen()
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "검색어를 입력하세요",
                    modifier = Modifier.padding(start = 16.dp)
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search button",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp)
                )
            }
            Spacer(Modifier.padding(4.dp))
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification Button",
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
            )
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
private fun TradeScreenPostButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = Indigo100,
    ) {
        Icon(Icons.Filled.Add, "Add Post Button")
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
                            itemStatus = ""
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}
