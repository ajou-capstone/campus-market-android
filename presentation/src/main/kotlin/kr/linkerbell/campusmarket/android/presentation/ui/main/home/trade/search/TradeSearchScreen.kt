package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space4
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result.TradeSearchResultConstant

@Composable
fun TradeSearchScreen(
    navController: NavController,
    argument: TradeSearchArgument,
    data: TradeSearchData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var queryString by remember { mutableStateOf(data.previousQuery) }

    val navigateToResultScreen = { queryName: String ->
        val newRoute = makeRoute(
            route = TradeSearchResultConstant.ROUTE,
            arguments = mapOf(
                TradeSearchResultConstant.ROUTE_ARGUMENT_NAME to queryName
            )
        )
        navController.popBackStack()
        navController.navigate(newRoute)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        TradeSearchScreenSearchBar(
            navController = navController,
            currentQuery = queryString,
            onValueChange = { updatedQuery ->
                queryString = updatedQuery
            },
            onSearchIconClick = { queryString ->
                argument.intent(TradeSearchIntent.Insert(queryString))
                navigateToResultScreen(queryString)
            }
        )

        Column {
            Spacer(modifier = Modifier.height(Space8))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space20),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "최근 검색 내역",
                    style = Headline3.merge(Gray900)
                )
                ConfirmButton(
                    properties = ConfirmButtonProperties(
                        size = ConfirmButtonSize.Small,
                        type = ConfirmButtonType.Secondary
                    ),
                    onClick = {
                        argument.intent(TradeSearchIntent.DeleteAll)
                    }
                ) { style ->
                    Text(
                        text = "전체 삭제",
                        style = style
                    )
                }
            }
            Spacer(modifier = Modifier.height(Space4))

            data.searchHistory.reversed().forEach { searchHistory ->
                TradeSearchScreenSearchHistoryCard(
                    history = searchHistory,
                    onClearIconClick = {
                        argument.intent(TradeSearchIntent.DeleteByText(searchHistory))
                    },
                    onTextClick = { selectedHistory ->
                        queryString = selectedHistory
                        navigateToResultScreen(selectedHistory)
                    }
                )
            }
        }
    }
}

@Composable
private fun TradeSearchScreenSearchBar(
    navController: NavController,
    currentQuery: String,
    onValueChange: (String) -> Unit,
    onSearchIconClick: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

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
            text = currentQuery,
            onValueChange = { onValueChange(it) },
            hintText = "검색어를 입력하세요",
            maxLines = 1,
            maxTextLength = 100,
            trailingIconContent = {
                if (currentQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        tint = Gray900,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onValueChange("")
                            }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (currentQuery.isNotBlank()) {
                        onSearchIconClick(currentQuery)
                    }
                }
            ),
            modifier = Modifier
                .padding(horizontal = Space20)
                .weight(1f)
                .focusRequester(focusRequester)
        )

        RippleBox(
            modifier = Modifier.padding(end = Space20),
            onClick = {
                if (currentQuery.isNotBlank()) {
                    onSearchIconClick(currentQuery)
                }
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun TradeSearchScreenSearchHistoryCard(
    history: String,
    onClearIconClick: () -> Unit,
    onTextClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTextClick(history)
            }
    ) {
        Spacer(modifier = Modifier.height(Space8))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(Space20))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = "Clock Image",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(12.dp)
                )
                Text(
                    text = history,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            RippleBox(
                onClick = {
                    onClearIconClick()
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space24),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = Gray900
                )
            }
            Spacer(modifier = Modifier.width(Space20))
        }
        Spacer(modifier = Modifier.height(Space8))
    }
}

@Preview
@Composable
private fun TradeSearchScreenPreview() {
    TradeSearchScreen(
        navController = rememberNavController(),
        argument = TradeSearchArgument(
            state = TradeSearchState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = TradeSearchData(
            listOf("history1", "history2", "history3"),
            ""
        )
    )
}
