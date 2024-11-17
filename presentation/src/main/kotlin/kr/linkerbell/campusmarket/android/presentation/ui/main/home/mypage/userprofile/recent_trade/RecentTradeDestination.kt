package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade

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

fun NavGraphBuilder.recentTradeDestination(
    navController: NavController
) {
    composable(
        route = RecentTradeConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(RecentTradeConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: RecentTradeViewModel = hiltViewModel()

        val argument: RecentTradeArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            RecentTradeArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: RecentTradeData = let {
            val recentTrades = viewModel.recentTrades.collectAsLazyPagingItems()

            RecentTradeData(
                recentTrades = recentTrades
            )
        }

        ErrorObserver(viewModel)
        RecentTradeScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}