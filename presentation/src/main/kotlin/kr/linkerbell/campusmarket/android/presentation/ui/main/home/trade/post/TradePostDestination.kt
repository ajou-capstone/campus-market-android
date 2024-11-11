package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.tradePostDestination(
    navController: NavController
) {
    composable(
        route = TradePostConstant.ROUTE,
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

        val data: TradePostData = let {
            val categoryList = viewModel.categoryList.value

            TradePostData(
                categoryList = categoryList
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