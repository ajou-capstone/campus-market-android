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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.TradeSearchConstant

@Composable
fun TradeSearchResultScreen(
    navController: NavController,
    argument: TradeSearchResultArgument,
    data: TradeSearchResultData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val currentQuery by remember { mutableStateOf(data.currentQuery) }

    val categoryList = data.categoryList

    val updateCurrentQuery = { updatedQuery: TradeSearchQuery ->
        argument.intent(TradeSearchResultIntent.ApplyNewQuery(updatedQuery))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        TradeSearchResultSearchBar(currentQuery.name) {
            navController.popBackStack()
            navController.navigate(TradeSearchConstant.ROUTE)
        }
        Box(modifier = Modifier.padding(16.dp)) {
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
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
        ) {
            items(
                count = data.tradeList.itemCount,
                key = { index -> data.tradeList[index]?.itemId ?: -1 }
            ) { index ->
                val trade = data.tradeList[index] ?: return@items
                TradeSearchResultItemCard(trade)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TradeSearchResultSearchBar(
    currentQuery: String, navigateToTradeSearchScreen: () -> Unit
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
                    text = currentQuery,
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
private fun TradeSearchResultPriceFilter(
    currentQuery: TradeSearchQuery,
    updateQuery: (TradeSearchQuery) -> Unit
) {

    var minPrice by remember { mutableIntStateOf(currentQuery.minPrice) }
    var maxPrice by remember { mutableIntStateOf(currentQuery.maxPrice) }

    Box {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "가격대",
                style = Headline3
            )
            Spacer(modifier = Modifier.padding(2.dp))
            TypingTextField(
                text = minPrice.toString(),
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        minPrice = newValue.toInt()
                    }
                },
                hintText = "0",
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
                text = maxPrice.toString(),
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        maxPrice = newValue.toInt()
                    }
                },
                hintText = "999,999,999",
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
                        updateQuery(
                            currentQuery.copy(
                                minPrice = minPrice,
                                maxPrice = maxPrice
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
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
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
                            currentQuery.copy(category = categoryList[index])
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
        Text("정렬", style = Headline3, modifier = Modifier.padding(end = 12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SortOptionButton(
                optionName = "최신순",
                isSelected = selectedSortedOption == sortByLatest,
                onSelect = {
                    selectedSortedOption = sortByLatest
                    updateQuery(currentQuery.copy(sorted = selectedSortedOption))
                }
            )
            SortOptionButton(
                optionName = "낮은 가격순",
                isSelected = selectedSortedOption == sortByLowPrice,
                onSelect = {
                    selectedSortedOption = sortByLowPrice
                    updateQuery(currentQuery.copy(sorted = selectedSortedOption))
                }
            )
            SortOptionButton(
                optionName = "높은 가격순",
                isSelected = selectedSortedOption == sortByHighPrice,
                onSelect = {
                    selectedSortedOption = sortByHighPrice
                    updateQuery(currentQuery.copy(sorted = selectedSortedOption))
                }
            )
        }
    }
}

@Composable
private fun TradeSearchResultItemCard(item: Trade) {
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
private fun TradeSearchResultItemStatus(isSold: Boolean) {
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
private fun SortOptionButton(
    optionName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val buttonColor = if (isSelected) Blue400 else Blue100

    Box(
        modifier = Modifier
            .padding(4.dp)
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
        "ACCESSORIES" -> "액세서리"
        "BOOKS_EDUCATIONAL_MATERIALS" -> "서적/교육 자료"
        "STATIONERY_OFFICE_SUPPLIES" -> "문구/사무용품"
        "HOUSEHOLD_ITEMS" -> "생활용품"
        "KITCHEN_SUPPLIES" -> "주방용품"
        "FURNITURE_INTERIOR" -> "가구/인테리어"
        "SPORTS_LEISURE" -> "스포츠/레저"
        "ENTERTAINMENT_HOBBIES" -> "엔터테인먼트/취미"
        "OTHER" -> "기타"
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
            tradeList = MutableStateFlow(
                PagingData.from(
                    listOf(
                        Trade(
                            itemId = 1L,
                            userId = 1L,
                            nickname = "유저22",
                            thumbnail = "https://picsum.photos/200",
                            title = "콜라 팝니다 근데_제목이_좀_길어서_이렇게_넘어가면_어케됨",
                            price = 1000,
                            chatCount = 5,
                            likeCount = 2,
                            itemStatus = ""
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
