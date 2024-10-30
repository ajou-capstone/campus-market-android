package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradesearch

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.tradeSearchDestination(
    navController: NavController
) {
    composable(
        route = TradeSearchConstant.ROUTE,
    ) {
        val viewModel: TradeSearchViewModel = hiltViewModel()

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
}
