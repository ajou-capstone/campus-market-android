package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.registerTermDestination(
    navController: NavController
) {
    composable(
        route = RegisterTermConstant.ROUTE
    ) {
        val viewModel: RegisterTermViewModel = hiltViewModel()

        val argument: RegisterTermArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            RegisterTermArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: RegisterTermData = let {
            val termList by viewModel.termList.collectAsStateWithLifecycle()

            RegisterTermData(
                termList = termList
            )
        }

        ErrorObserver(viewModel)
        RegisterTermScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
