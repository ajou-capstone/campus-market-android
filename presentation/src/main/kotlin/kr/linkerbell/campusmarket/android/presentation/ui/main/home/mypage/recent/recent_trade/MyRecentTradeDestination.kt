package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade

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
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeArgument

fun NavGraphBuilder.myRecentTradeDestination(
    navController: NavController
) {
    composable(
        route = MyRecentTradeConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(MyRecentTradeConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: MyRecentTradeViewModel = hiltViewModel()

        val argument: MyRecentTradeArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            MyRecentTradeArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: MyRecentTradeData = let {
            val recentTradeList = viewModel.recentTrades.collectAsLazyPagingItems()
            val recentBuyTradeList = viewModel.recentBuyTrades.collectAsLazyPagingItems()
            val recentSellTradeList = viewModel.recentSellTrades.collectAsLazyPagingItems()
            val myId by viewModel.userId.collectAsStateWithLifecycle()

            MyRecentTradeData(
                recentTrades = recentTradeList,
                recentBuyTrades = recentBuyTradeList,
                recentSellTrades = recentSellTradeList,
                myId = myId
            )
        }

        ErrorObserver(viewModel)
        MyRecentTradeScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
