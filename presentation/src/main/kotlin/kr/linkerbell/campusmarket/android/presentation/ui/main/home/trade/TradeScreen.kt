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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.TradeSearchConstant

@Composable
fun TradeScreen(
    navController: NavController,
    viewModel: TradeViewModel = hiltViewModel(),
) {
    val argument: LandingPageArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LandingPageArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: TradeData = Unit.let {
        val tradeList = viewModel.tradeList.collectAsLazyPagingItems()

        TradeData(
            tradeList = tradeList,
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
    argument: LandingPageArgument,
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
            items(
                count = data.tradeList.itemCount,
                key = { index -> data.tradeList[index]?.itemId ?: -1 }
            ) { index ->
                val trade = data.tradeList[index] ?: return@items
                TradeItemCard(trade)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TradeItemCard(item: Trade) {
    Box(
        Modifier
            .shadow(4.dp)
            .clip(RoundedCornerShape(5.dp))
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
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = item.title,
                            style = Headline3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        TradeItemStatus(isSold = item.itemStatus === "Available")
                    }
                    Text("${item.price} 원")
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

@Preview
@Composable
private fun TradeScreenPreview() {
    TradeScreen(
        navController = rememberNavController(),
        argument = LandingPageArgument(
            state = LandingPageState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = TradeData(
            tradeList = MutableStateFlow(
                PagingData.from(
                    listOf(
                        Trade(
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
