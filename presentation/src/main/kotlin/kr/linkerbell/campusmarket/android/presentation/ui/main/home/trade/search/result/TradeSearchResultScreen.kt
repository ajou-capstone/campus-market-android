package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue500
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeSearchResultScreen(
    navController: NavController,
    argument: TradeSearchResultArgument,
    data: TradeSearchResultData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var currentQuery by remember { mutableStateOf(data.currentQuery) }
    val categoryList = listOf("") + data.categoryList
    var isPriceFilterAvailable by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()

    val updateCurrentQuery = { updatedQuery: TradeSearchQuery ->
        currentQuery = updatedQuery
        argument.intent(
            TradeSearchResultIntent.ApplyNewQuery(
                updatedQuery.copy(
                    minPrice = if (isPriceFilterAvailable) currentQuery.minPrice else 0,
                    maxPrice = if (isPriceFilterAvailable) currentQuery.maxPrice else Int.MAX_VALUE
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        TradeSearchResultSearchBar(
            navController = navController,
            initialQuery = data.currentQuery.name,
            onQueryChanged = {
                updateCurrentQuery(currentQuery.copy(name = it))
            },
        )
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            Column {
                TradeSearchResultPriceFilter(
                    currentQuery = currentQuery,
                    updateQuery = updateCurrentQuery,
                    isPriceFilterAvailable = { isChecked ->
                        isPriceFilterAvailable = isChecked
                    }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Row {
                    Box(modifier = Modifier.weight(2f)) {
                        TradeSearchResultSortOption(
                            currentQuery = currentQuery,
                            updateQuery = updateCurrentQuery
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Box(modifier = Modifier.weight(3f)) {
                        TradeSearchResultCategoryFilter(
                            currentQuery = currentQuery,
                            categoryList = categoryList,
                            updateQuery = updateCurrentQuery
                        )
                    }
                }

            }
        }
        HorizontalDivider(
            thickness = (0.4).dp,
            color = Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (data.summarizedTradeList.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "표시할 항목이 없습니다.",
                    style = Body0,
                    color = Gray,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .nestedScroll(refreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
            ) {

                items(
                    count = data.summarizedTradeList.itemCount,
                    key = { index -> data.summarizedTradeList[index]?.itemId ?: -1 }
                ) { index ->
                    val trade = data.summarizedTradeList[index] ?: return@items
                    TradeSearchResultItemCard(
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
                argument.intent(TradeSearchResultIntent.RefreshNewTrades)
                refreshState.endRefresh()
            }
            if (refreshState.progress > 0 || refreshState.isRefreshing) {
                PullToRefreshContainer(
                    state = refreshState,
                    containerColor = White,
                    contentColor = Blue200,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )
            }
        }
    }
    LaunchedEffectWithLifecycle(coroutineContext) {
        argument.intent(TradeSearchResultIntent.RefreshNewTrades)
    }
}

@Composable
private fun TradeSearchResultSearchBar(
    navController: NavController,
    initialQuery: String,
    onQueryChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialQuery) }

    Row(
        modifier = Modifier
            .height(Space56)
            .background(White)
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
        TypingTextField(
            text = text,
            onValueChange = { text = it },
            hintText = "검색어를 입력하세요",
            maxLines = 1,
            maxTextLength = 100,
            trailingIconContent = {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        tint = Gray900,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                text = ""
                            }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onQueryChanged(text)
                }
            ),
            modifier = Modifier
                .padding(horizontal = Space20)
                .weight(1f)
        )

        RippleBox(
            modifier = Modifier.padding(end = Space20),
            onClick = {
                onQueryChanged(text)
            }
        ) {
            Icon(
                modifier = Modifier.size(Space24),
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = Gray900
            )
        }
    }
}

@Composable
private fun TradeSearchResultPriceFilter(
    currentQuery: TradeSearchQuery,
    updateQuery: (TradeSearchQuery) -> Unit,
    isPriceFilterAvailable: (Boolean) -> Unit
) {
    var minPrice by remember { mutableIntStateOf(currentQuery.minPrice) }
    var maxPrice by remember { mutableIntStateOf(currentQuery.maxPrice) }
    var minPriceInText by remember { mutableStateOf(if (minPrice == 0) "" else minPrice.toString()) }
    var maxPriceInText by remember { mutableStateOf(if (maxPrice == Int.MAX_VALUE) "" else maxPrice.toString()) }

    var isPriceFilterChecked by remember { mutableStateOf(false) }

    val updatePriceQuery = {
        if (minPrice > maxPrice) {
            maxPrice = minPrice
        }
        updateQuery(
            currentQuery.copy(
                minPrice = minPrice,
                maxPrice = maxPrice,
            )
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "가격",
                style = Headline3,
                color = Black
            )
            Spacer(modifier = Modifier.padding(8.dp))
            TypingTextField(
                text = minPriceInText,
                onValueChange = { newValue ->
                    if (newValue.isBlank()) {
                        minPrice = 0
                        minPriceInText = ""
                    } else {
                        val filteredValue = newValue.filter { it.isDigit() }
                        minPrice = filteredValue.toIntOrNull() ?: 0
                        minPriceInText = filteredValue
                        if (isPriceFilterChecked) {
                            updatePriceQuery()
                        }
                    }
                },
                hintText = "최소 가격",
                maxLines = 1,
                maxTextLength = 9,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                "~", modifier = Modifier.padding(horizontal = 4.dp)
            )
            TypingTextField(
                text = maxPriceInText,
                onValueChange = { newValue ->
                    if (newValue.isBlank()) {
                        maxPrice = 0
                        maxPriceInText = ""
                    } else {
                        val filteredValue = newValue.filter { it.isDigit() }
                        maxPrice = filteredValue.toIntOrNull() ?: 1000000
                        maxPriceInText = filteredValue
                        if (isPriceFilterChecked) {
                            updatePriceQuery()
                        }
                    }
                },
                hintText = "최대 가격",
                maxLines = 1,
                maxTextLength = 9,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "적용",
                style = Body1,
                color = Black
            )
            Checkbox(
                checked = isPriceFilterChecked,
                onCheckedChange = {
                    if (minPrice > maxPrice) {
                        maxPrice = minPrice
                    }
                    isPriceFilterChecked = it
                    isPriceFilterAvailable(isPriceFilterChecked)
                    updateQuery(
                        currentQuery.copy(
                            minPrice = minPrice,
                            maxPrice = maxPrice,
                        )
                    )
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Blue400,
                    uncheckedColor = Gray900
                )
            )
        }
    }
}

@Composable
private fun TradeSearchResultCategoryFilter(
    currentQuery: TradeSearchQuery,
    categoryList: List<String>,
    updateQuery: (TradeSearchQuery) -> Unit
) {

    var isDropDownExpanded by remember { mutableStateOf(false) }
    val itemIndex = remember {
        mutableIntStateOf(
            categoryList.indexOf(currentQuery.category).takeIf { it >= 0 } ?: 0
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "카테고리",
            style = Headline3,
            color = Black
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Blue400)
                .border(1.dp, Blue500, shape = RoundedCornerShape(4.dp))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .clickable {
                        isDropDownExpanded = !isDropDownExpanded
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = translateToKor(categoryList[itemIndex.intValue]),
                    maxLines = 1,
                    style = Body1,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Search Button",
                    tint = Gray900,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                categoryList.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        text = { Text(text = translateToKor(category)) },
                        onClick = {
                            isDropDownExpanded = false
                            itemIndex.intValue = index
                            updateQuery(
                                currentQuery.copy(category = categoryList[index])
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TradeSearchResultSortOption(
    currentQuery: TradeSearchQuery,
    updateQuery: (TradeSearchQuery) -> Unit
) {
    val sortOption = listOf("createdDate,desc", "price,asc", "price,desc")
    val sortOptionKor = listOf("최신순", "낮은 가격순", "높은 가격순")

    var isDropDownExpanded by remember { mutableStateOf(false) }
    val itemIndex = remember {
        mutableIntStateOf(
            sortOption.indexOf(currentQuery.sorted).takeIf { it >= 0 } ?: 0
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "정렬",
            style = Headline3,
            color = Black
        )
        Spacer(modifier = Modifier.padding(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Blue400)
                .border(1.dp, Blue500, shape = RoundedCornerShape(4.dp))
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .border(1.dp, Blue500, shape = RoundedCornerShape(4.dp))
                    .clickable {
                        isDropDownExpanded = !isDropDownExpanded
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sortOptionKor[itemIndex.intValue],
                    maxLines = 1,
                    style = Body1,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Search Button",
                    tint = Gray900,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false }
            ) {
                sortOptionKor.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            isDropDownExpanded = false
                            itemIndex.intValue = index
                            updateQuery(
                                currentQuery.copy(sorted = sortOption[index])
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TradeSearchResultItemCard(
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
                        TradeSearchResultItemStatus(
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
private fun TradeSearchResultItemStatus(isSold: Boolean) {
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

private fun translateToKor(engCategory: String): String {
    return when (engCategory) {
        "ELECTRONICS_IT" -> "전자기기/IT"
        "HOME_APPLIANCES" -> "가전제품"
        "FASHION_ACCESSORIES" -> "패션/액세서리"
        "BOOKS_EDUCATIONAL_MATERIALS" -> "서적/교육 자료"
        "STATIONERY_OFFICE_SUPPLIES" -> "문구/사무용품"
        "HOUSEHOLD_ITEMS" -> "생활용품"
        "KITCHEN_SUPPLIES" -> "주방용품"
        "FURNITURE_INTERIOR" -> "가구/인테리어"
        "SPORTS_LEISURE" -> "스포츠/레저"
        "ENTERTAINMENT_HOBBIES" -> "엔터테인먼트/취미"
        "OTHER" -> "기타"
        "" -> "전체"
        else -> "기타"
    }
}

@Preview
@Composable
private fun TradeSearchResultScreenPreview() {
    TradeSearchResultScreen(
        navController = rememberNavController(),
        argument = TradeSearchResultArgument(
            state = TradeSearchResultState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = TradeSearchResultData(
            summarizedTradeList = MutableStateFlow(
                PagingData.from(
                    listOf(
                        SummarizedTrade(
                            itemId = 1L,
                            userId = 1L,
                            nickname = "유저22",
                            thumbnail = "https://picsum.photos/200",
                            title = "콜라 팝니다 근데_제목이_좀_길어서_이렇게_넘어가면_어케됨",
                            price = 1000,
                            chatCount = 5,
                            likeCount = 2,
                            itemStatus = "",
                            isLiked = true
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            currentQuery = TradeSearchQuery(
                name = "콜라"
            ),
            categoryList = CategoryList.empty.categoryList
        )
    )
}
