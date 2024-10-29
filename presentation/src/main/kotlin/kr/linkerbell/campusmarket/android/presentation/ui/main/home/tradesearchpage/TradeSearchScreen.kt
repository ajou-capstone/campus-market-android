package kr.linkerbell.campusmarket.android.presentation.ui.main.home.tradesearchpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

@Composable
fun TradeSearchScreen(
    navController: NavController,
    viewModel: TradeSearchViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.fetchSearchHistory()
    }
    val argument: TradeSearchArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        TradeSearchArgument(
            state = state,
            event = viewModel.event,
            intent = { intent -> viewModel.onIntent(intent) },
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: TradeSearchData = Unit.let {
        val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()

        TradeSearchData(
            searchHistory = searchHistory
        )
    }

    ErrorObserver(viewModel)
    TradeSearchScreen(
        navController = navController,
        argument = argument,
        data = data
    )
}

@Composable
fun TradeSearchScreen(
    navController: NavController,
    argument: TradeSearchArgument,
    data: TradeSearchData
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Indigo50)
    ) {
        TradeSearchScreenSearchBar()

        Column(
            modifier = Modifier.padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("최근 검색 내역")
                Text(text = "전체 삭제",
                    modifier = Modifier.clickable {
                        argument.intent(TradeSearchIntent.DeleteAll)
                    }
                )
            }

            data.searchHistory.forEach { searchHistory ->
                TradeSearchScreenSearchHistoryCard(
                    searchHistory,
                    onClick = { argument.intent(TradeSearchIntent.DeleteByText(searchHistory)) }
                )
            }
        }
    }
}

@Composable
private fun TradeSearchScreenSearchBar() {

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
            var text by remember { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .weight(8f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Indigo50),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier,
                    textStyle = TextStyle(fontSize = 12.sp),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "검색어를 입력하세요",
                                    style = TextStyle(color = Color.Black)
                                )
                            }
                            innerTextField()
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear button",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(20.dp)
                            )
                        }
                    }
                )
            }
            Spacer(Modifier.padding(4.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Button",
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun TradeSearchScreenSearchHistoryCard(history: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Clear button",
            modifier = Modifier
                .padding(end = 8.dp)
                .size(20.dp)
                .clickable {
                    onClick()
                }
        )
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
            listOf("history1", "history2", "history3")
        )
    )
}
