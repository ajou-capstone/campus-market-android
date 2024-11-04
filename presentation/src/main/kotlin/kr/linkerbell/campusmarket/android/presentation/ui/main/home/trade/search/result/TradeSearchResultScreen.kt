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
import androidx.compose.ui.res.painterResource
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
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue700
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        TradeSearchResultSearchBar(
            currentSearchText = data.currentTradeSearchQuery.name
        ) {
            navController.popBackStack()
            navController.navigate(TradeSearchConstant.ROUTE)
        }
        TradeSearchResultFilterGroup(
            data.currentTradeSearchQuery,
            applyCategoryOption = { newQueryOption ->
                argument.intent(TradeSearchResultIntent.ApplyCategoryFilter(newQueryOption))
            },
            applyMinPriceOption = { newQueryOption ->
                argument.intent(TradeSearchResultIntent.ApplyMinPriceFilter(newQueryOption))
            },
            applyMaxPriceOption = { newQueryOption ->
                argument.intent(TradeSearchResultIntent.ApplyMaxPriceFilter(newQueryOption))
            },
            applySortOption = { newQueryOption ->
                argument.intent(TradeSearchResultIntent.ApplySortingFilter(newQueryOption))
            }
        )
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
private fun TradeSearchResultFilterGroup(
    currentQuery: TradeSearchQuery,
    applyCategoryOption: (String) -> Unit,
    applyMinPriceOption: (Int) -> Unit,
    applyMaxPriceOption: (Int) -> Unit,
    applySortOption: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(3f)) {
                PriceFilterMenu(
                    currentQuery.minPrice,
                    currentQuery.maxPrice,
                    applyMinPriceOption,
                    applyMaxPriceOption,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                CategoryDropDownMenu(
                    currentQuery.category,
                    //TODO("TradeSearchResultScreen : Category List 가져오기")
                    listOf("ct1", "ct2", "ct3"),
                    applyCategoryOption
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        SortOptionMenu(currentQuery.sorted, applySortOption)
    }
}

@Composable
private fun PriceFilterMenu(
    currentMinPrice: Int,
    currentMaxPrice: Int,
    applyMinPriceOption: (Int) -> Unit,
    applyMaxPriceOption: (Int) -> Unit
) {
    var minPrice by remember { mutableStateOf(currentMinPrice.toString()) }
    var maxPrice by remember { mutableStateOf(currentMaxPrice.toString()) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("가격대")
        Spacer(modifier = Modifier.padding(2.dp))
        TypingTextField(
            text = minPrice,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    minPrice = newValue
                }
            },
            hintText = "0",
            maxLines = 1,
            maxTextLength = 9,
            modifier = Modifier.weight(3f)
        )
        Text(
            "~", modifier = Modifier.padding(horizontal = 4.dp)
        )
        TypingTextField(
            text = maxPrice,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    maxPrice = newValue
                }
            },
            hintText = "999,999,999",
            maxLines = 1,
            maxTextLength = 9,
            modifier = Modifier.weight(3f)
        )
        Icon(imageVector = Icons.Default.Search,
            contentDescription = "Search Button",
            modifier = Modifier
                .size(24.dp)
                .weight(1f)
                .clickable {
                    applyMinPriceOption(minPrice.toInt())
                    applyMaxPriceOption(maxPrice.toInt())
                }
        )


    }
}

@Composable
private fun SortOptionMenu(currentOption: String, applySortOption: (String) -> Unit) {
    val sortByLatest = "createdDate,desc"   // 최신순
    val sortByLowPrice = "price,asc"        // 낮은 가격순
    val sortByHighPrice = "price,desc"      // 높은 가격순
    var selectedOption by remember { mutableStateOf(currentOption) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("정렬", modifier = Modifier.padding(end = 12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SortOptionButton(
                optionName = "최신순",
                isSelected = selectedOption == sortByLatest,
                onSelect = {
                    selectedOption = sortByLatest
                    applySortOption(sortByLatest)
                }
            )
            SortOptionButton(
                optionName = "낮은 가격순",
                isSelected = selectedOption == sortByLowPrice,
                onSelect = {
                    selectedOption = sortByLowPrice
                    applySortOption(sortByLowPrice)
                }
            )
            SortOptionButton(
                optionName = "높은 가격순",
                isSelected = selectedOption == sortByHighPrice,
                onSelect = {
                    selectedOption = sortByHighPrice
                    applySortOption(sortByHighPrice)
                }
            )
        }
    }
}

@Composable
private fun SortOptionButton(
    optionName: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val buttonColor = if (isSelected) Color.White else Color.Gray

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(buttonColor)
            .clickable { onSelect() }
    ) {
        Text(
            text = optionName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun CategoryDropDownMenu(
    currentOption: String,
    categoryList: List<String>,
    applyCategoryOption: (String) -> Unit
) {
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(currentOption) }
    var categoryIndex: Int by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Gray50)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedCategory.ifBlank { "전체" },
                style = Body1,
                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Search Button",
                modifier = Modifier
                    .size(24.dp)
            )
        }
        DropdownMenu(
            modifier = Modifier.background(White),
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false },
        ) {
            categoryList.mapIndexed { index, category ->
                val categoryName = category.ifBlank { "전체" }
                DropdownMenuItem(
                    text = {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = Space8),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = categoryName,
                                    style = Body1.merge(Gray900)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                if (index == categoryIndex) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_check_line),
                                        contentDescription = null,
                                        tint = Blue700
                                    )
                                }
                            }
                        }
                    },
                    onClick = {
                        categoryIndex = index
                        selectedCategory = categoryList[categoryIndex]
                        isDropDownMenuExpanded = false
                        applyCategoryOption(selectedCategory)
                    },
                    contentPadding = PaddingValues(0.dp)
                )
            }
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
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = item.title,
                            style = Headline3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        TradeSearchResultItemStatus(isSold = item.itemStatus === "Available")
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
private fun TradeSearchResultSearchBar(
    currentSearchText: String,
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
                    text = currentSearchText,
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
private fun TradeSearchResultItemStatus(
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
            ).collectAsLazyPagingItems(),
            currentTradeSearchQuery = TradeSearchQuery(
                name = "콜라"
            )
        )
    )
}
