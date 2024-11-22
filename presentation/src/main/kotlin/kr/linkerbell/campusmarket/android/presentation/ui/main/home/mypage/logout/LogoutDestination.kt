package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.logoutDestination(
    navController: NavController
) {
    composable(
        route = LogoutConstant.ROUTE
    ) {
        val viewModel: LogoutViewModel = hiltViewModel()

        val argument: LogoutArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            LogoutArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        ErrorObserver(viewModel)
        LogoutScreen(
            navController = navController,
            argument = argument,
        )
    }
}
