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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
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
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant

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

    val updateCurrentQuery = { updatedQuery: TradeSearchQuery ->
        currentQuery = updatedQuery
        argument.intent(TradeSearchResultIntent.ApplyNewQuery(updatedQuery))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        TradeSearchResultSearchBar(
            navController = navController,
            initialQuery = data.currentQuery.name,
            onQueryChanged = {
                updateCurrentQuery(currentQuery.copy(name = it))
            },
        )
        Box(modifier = Modifier.padding(20.dp)) {
            Column {
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(3f)) {
                        TradeSearchResultPriceFilter(
                            currentQuery = currentQuery,
                            updateQuery = updateCurrentQuery
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        TradeSearchResultCategoryFilter(
                            currentQuery = currentQuery,
                            categoryList = categoryList,
                            updateQuery = updateCurrentQuery
                        )
                    }
                }
                TradeSearchResultSortOption(
                    currentQuery = currentQuery,
                    updateQuery = updateCurrentQuery
                )
            }
        }
        HorizontalDivider(thickness = (0.4).dp, color = Black)
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
        LazyColumn(
            modifier = Modifier.weight(1f),
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
    updateQuery: (TradeSearchQuery) -> Unit
) {

    var minPrice by remember { mutableIntStateOf(currentQuery.minPrice) }
    var maxPrice by remember { mutableIntStateOf(currentQuery.maxPrice) }
    var minPriceInText by remember { mutableStateOf(if (minPrice == 0) "" else minPrice.toString()) }
    var maxPriceInText by remember { mutableStateOf(if (maxPrice == Int.MAX_VALUE) "" else maxPrice.toString()) }

    Box {
        Row(
            modifier = Modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "가격",
                style = Headline3,
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
                    }
                },
                hintText = "최소 가격",
                maxLines = 1,
                maxTextLength = 9,
                modifier = Modifier.weight(3f),
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
                    }
                },
                hintText = "최대 가격",
                maxLines = 1,
                maxTextLength = 9,
                modifier = Modifier.weight(3f),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Icon(imageVector = Icons.Default.Search,
                contentDescription = "Search Button",
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
                    .clickable {
                        if (minPrice > maxPrice) {
                            maxPrice = minPrice
                        }
                        updateQuery(
                            currentQuery.copy(
                                minPrice = minPrice,
                                maxPrice = maxPrice,
                                category = currentQuery.category,
                                sorted = currentQuery.sorted
                            )
                        )
                    }
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

    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemIndex = remember {
        mutableIntStateOf(
            categoryList.indexOf(currentQuery.category).takeIf { it >= 0 } ?: 0
        )
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Blue400)
            .border(1.dp, Gray, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = translateToKor(categoryList[itemIndex.intValue]),
                maxLines = 1,
                style = Body2,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Search Button",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        isDropDownExpanded.value = !isDropDownExpanded.value
                    }
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded.value,
            onDismissRequest = { isDropDownExpanded.value = false }
        ) {
            categoryList.forEachIndexed { index, category ->
                DropdownMenuItem(
                    text = { Text(text = translateToKor(category)) },
                    onClick = {
                        isDropDownExpanded.value = false
                        itemIndex.intValue = index
                        updateQuery(
                            currentQuery.copy(
                                minPrice = currentQuery.minPrice,
                                maxPrice = currentQuery.maxPrice,
                                category = categoryList[index],
                                sorted = currentQuery.sorted
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TradeSearchResultSortOption(
    currentQuery: TradeSearchQuery,
    updateQuery: (TradeSearchQuery) -> Unit
) {
    val sortByLatest = "createdDate,desc"   // 최신순
    val sortByLowPrice = "price,asc"        // 낮은 가격순
    val sortByHighPrice = "price,desc"      // 높은 가격순

    var selectedSortedOption by remember { mutableStateOf(currentQuery.sorted) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("정렬", style = Headline3)
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SortOptionButton(
                optionName = "최신순",
                isSelected = selectedSortedOption == sortByLatest,
                onSelect = {
                    selectedSortedOption = sortByLatest
                    updateQuery(
                        currentQuery.copy(
                            minPrice = currentQuery.minPrice,
                            maxPrice = currentQuery.maxPrice,
                            category = currentQuery.category,
                            sorted = selectedSortedOption
                        )
                    )
                }
            )
            SortOptionButton(
                optionName = "낮은 가격순",
                isSelected = selectedSortedOption == sortByLowPrice,
                onSelect = {
                    selectedSortedOption = sortByLowPrice
                    updateQuery(
                        currentQuery.copy(
                            minPrice = currentQuery.minPrice,
                            maxPrice = currentQuery.maxPrice,
                            category = currentQuery.category,
                            sorted = selectedSortedOption
                        )
                    )
                }
            )
            SortOptionButton(
                optionName = "높은 가격순",
                isSelected = selectedSortedOption == sortByHighPrice,
                onSelect = {
                    selectedSortedOption = sortByHighPrice
                    updateQuery(
                        currentQuery.copy(
                            minPrice = currentQuery.minPrice,
                            maxPrice = currentQuery.maxPrice,
                            category = currentQuery.category,
                            sorted = selectedSortedOption
                        )
                    )
                }
            )
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
                        TradeSearchResultItemStatus(
                            isSold = item.itemStatus === "Available"
                        )
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
                    val favIcon =
                        if (item.isLiked) Icons.Default.FavoriteBorder else Icons.Default.Favorite
                    Icon(
                        imageVector = favIcon,
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

@Composable
private fun TradeSearchResultItemStatus(isSold: Boolean) {
    if (isSold) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Gray),
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
private fun SortOptionButton(
    optionName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val buttonColor = if (isSelected) Blue400 else Blue100

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(buttonColor)
            .clickable { onSelect() }
    ) {
        Text(
            text = optionName,
            style = Body1,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
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
