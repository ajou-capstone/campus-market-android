package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.tradeSearchResultDestination(
    navController: NavController,
) {
    composable(
        route = TradeSearchResultConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument("name") { type = NavType.StringType; defaultValue = "" },
            navArgument("category") { type = NavType.StringType; defaultValue = "" },
            navArgument("minPrice") { type = NavType.IntType; defaultValue = 0 },
            navArgument("maxPrice") { type = NavType.IntType; defaultValue = Int.MAX_VALUE },
            navArgument("sorted") { type = NavType.StringType; defaultValue = "" }
        )
    ) {
        val viewModel: TradeSearchResultViewModel = hiltViewModel()

        val argument: TradeSearchResultArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            TradeSearchResultArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: TradeSearchResultData = let {
            val tradeList = viewModel.summarizedTradeList.collectAsLazyPagingItems()
            val categoryList = viewModel.categoryList.value
            val currentQuery = viewModel.tradeSearchQuery.value

            TradeSearchResultData(
                summarizedTradeList = tradeList,
                currentQuery = currentQuery,
                categoryList = categoryList
            )
        }

        ErrorObserver(viewModel)
        TradeSearchResultScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
