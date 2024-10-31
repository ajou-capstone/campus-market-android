package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.compose.runtime.LaunchedEffect
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
            navArgument("maxPrice") { type = NavType.IntType; defaultValue = 0 },
            navArgument("sorted") { type = NavType.StringType; defaultValue = "createdDate,desc" }
        )
    ) { backStackEntry ->

        val tradeSearchQuery = TradeSearchQuery(
            name = backStackEntry.arguments?.getString("name") ?: "",
            category = backStackEntry.arguments?.getString("category") ?: "",
            minPrice = backStackEntry.arguments?.getInt("minPrice") ?: 0,
            //TODO("maxPrice의 디폴트 값은 얼마로 줄 지? + 가격 상한 걸어둘거?")
            maxPrice = backStackEntry.arguments?.getInt("maxPrice") ?: 0,
            sorted = backStackEntry.arguments?.getString("sorted") ?: "createdDate,desc"
        )

        val viewModel: TradeSearchResultViewModel = hiltViewModel()

        LaunchedEffect(tradeSearchQuery) {
            viewModel.onReceivedQuery(tradeSearchQuery)
        }

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
            val tradeList = viewModel.tradeList.collectAsLazyPagingItems()

            TradeSearchResultData(
                tradeList = tradeList
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
