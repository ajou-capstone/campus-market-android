package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.withdrawalDestination(
    navController: NavController
) {
    composable(
        route = WithdrawalConstant.ROUTE,
        arguments = listOf(
            navArgument(WithdrawalConstant.ROUTE) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val viewModel: WithdrawalViewModel = hiltViewModel()

        val argument: WithdrawalArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            WithdrawalArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: WithdrawalData = let {
            val myProfile by viewModel.myProfile.collectAsStateWithLifecycle()

            WithdrawalData(
                myProfile = myProfile
            )
        }

        ErrorObserver(viewModel)
        WithdrawalScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
