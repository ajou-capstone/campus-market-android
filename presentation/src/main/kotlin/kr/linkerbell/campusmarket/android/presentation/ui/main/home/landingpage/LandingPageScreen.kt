package kr.linkerbell.campusmarket.android.presentation.ui.main.home.landingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

@Composable
fun LandingPageScreen(
    navController: NavController,
    viewModel: LandingPageViewModel = hiltViewModel(),
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
    val latestItemList by viewModel.latestItemsList.collectAsStateWithLifecycle()

    ErrorObserver(viewModel)
    LandingPageScreen(
        navController = navController,
        argument = argument,
        latestItemList = latestItemList,
        loadMoreItems = { viewModel.loadMoreItems() }
    )
}

@Composable
fun LandingPageScreen(
    navController: NavController,
    argument: LandingPageArgument,
    latestItemList: List<SummarizedItem>,
    loadMoreItems: () -> Unit
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size }
            .collect { visibleItemCount ->
                if (visibleItemCount >= latestItemList.size) {
                    loadMoreItems()
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8EAF6))
    ) {
        Column {
            LandingPageSearchBar()
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
            ) {
                items(latestItemList.size) { index ->
                    LandingPageItemCard(latestItemList[index])
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun LandingPageItemCard(item: SummarizedItem) {

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
            LandingPageLoadImageFromS3(item.thumbnail)
            Column(modifier = Modifier.padding(start = 10.dp)) {
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
                            landingPageSummarizeTitle(item.title),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        LandingPageItemStatus(isSold = false)
                    }
                    Text("${item.price} 원")
                }
                Text(
                    item.nickname,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    "${item.chatCount} 명이 대화중",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
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
fun LandingPageSearchBar() {

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
            //SearchBar
            Row(
                modifier = Modifier
                    .weight(8f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xffF7F7FB)),
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
fun LandingPageLoadImageFromS3(s3Url: String) {
    SubcomposeAsyncImage(
        model = s3Url,
        contentDescription = "Image from S3",
        contentScale = ContentScale.Crop,
        loading = {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "loading image",
                modifier = Modifier
                    .border(1.dp, Color.Gray)
                    .size(85.dp)
            )
            //CircularProgressIndicator()
        },
        error = {
            Text("err image")
            //load default image? 협의 필요
        }
    )
}

@Composable
fun LandingPageItemStatus(isSold: Boolean = false) {

    if (isSold) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp)) // 16.dp 만큼 둥근 모서리 설정
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("거래 완료", modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
        }
    } else {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp)) // 16.dp 만큼 둥근 모서리 설정
                .background(Color.LightGray)
        ) {
            Text("거래중", modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
        }
    }

}

fun landingPageSummarizeTitle(title: String): String {
    return if (title.length > 12) {
        title.take(12) + "..."
    } else {
        title
    }
}

@Preview
@Composable
fun LandingPageSearchBarPreview() {
    LandingPageSearchBar()
}

@Preview
@Composable
private fun LandingPageItemCardPreview() {
    LandingPageItemCard(
        SummarizedItem(
            1, 2, "Sirius22", "",
            "제목이 좀 길어 지면 다 표시 하지 말고 줄여야 할 것 같아요", 2222, 5, 123,
            "거래중"
        )
    )
}

@Preview
@Composable
private fun LandingPageScreenPreview() {
    val loadMoreData: () -> Unit = {}

    LandingPageScreen(
        navController = rememberNavController(),
        argument = LandingPageArgument(
            state = LandingPageState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        latestItemList = MutableList(5) { SummarizedItem.empty },
        loadMoreItems = loadMoreData
    )
}
