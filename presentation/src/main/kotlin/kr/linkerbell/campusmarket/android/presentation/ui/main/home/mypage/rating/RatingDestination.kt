package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.ratingDestination(
    navController: NavController
) {
    composable(
        route = RatingConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(RatingConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
                defaultValue = -1L
            },
            navArgument(RatingConstant.ROUTE_ARGUMENT_ITEM_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: RatingViewModel = hiltViewModel()

        val argument: RatingArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            RatingArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        ErrorObserver(viewModel)
        RatingScreen(
            navController = navController,
            argument = argument,
        )
    }
}
