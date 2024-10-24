package kr.linkerbell.campusmarket.android.presentation.ui.main.home.tradesearchpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

@Composable
fun TradeSearchScreen(
    navController: NavController,
    viewModel: TradeSearchViewModel = hiltViewModel()
) {
    val argument: TradeSearchArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        TradeSearchArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
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

    Column(modifier = Modifier.fillMaxSize()) {

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
