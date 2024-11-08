package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.tradeSearchDestination(
    navController: NavController
) {
    composable(
        route = TradeSearchConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument("name") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->

        val viewModel: TradeSearchViewModel = hiltViewModel()

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
                searchHistory = searchHistory,
                previousQuery = backStackEntry.arguments?.getString("name") ?: ""
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
