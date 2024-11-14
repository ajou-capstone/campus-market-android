package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.tradePostDestination(
    navController: NavController
) {
    composable(
        route = TradePostConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(TradePostConstant.ROUTE_ARGUMENT_ITEM_ID) {
                type = NavType.LongType
                defaultValue = -1L
            },
        )
    ) {
        val viewModel: TradePostViewModel = hiltViewModel()

        val argument: TradePostArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            TradePostArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: TradePostData = Unit.let {
            val categoryList by viewModel.categoryList.collectAsStateWithLifecycle()
            val originalTradePostInfo by viewModel.originalTradeContents.collectAsStateWithLifecycle()

            TradePostData(
                categoryList = categoryList,
                originalTradeContents = originalTradePostInfo
            )
        }

        ErrorObserver(viewModel)
        TradePostScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
