package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.tradeInfoDestination(
    navController: NavController
) {
    composable(
        route = TradeInfoConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: TradeInfoViewModel = hiltViewModel()

        val argument: TradeInfoArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            TradeInfoArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: TradeInfoData = let {
            val tradeInfo by viewModel.tradeInfo.collectAsStateWithLifecycle()
            val authorInfo by viewModel.authorInfo.collectAsStateWithLifecycle()
            val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()

            TradeInfoData(
                tradeInfo = tradeInfo,
                authorInfo = authorInfo,
                userInfo = userInfo
            )
        }

        ErrorObserver(viewModel)
        TradeInfoScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
