package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.university

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.registerUniversityDestination(
    navController: NavController
) {
    composable(
        route = RegisterUniversityConstant.ROUTE
    ) {
        val viewModel: RegisterUniversityViewModel = hiltViewModel()

        val argument: RegisterUniversityArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            RegisterUniversityArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        ErrorObserver(viewModel)
        RegisterUniversityScreen(
            navController = navController,
            argument = argument
        )
    }
}
