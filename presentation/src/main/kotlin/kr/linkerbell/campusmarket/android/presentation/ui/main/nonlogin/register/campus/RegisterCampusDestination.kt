package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.registerCampusDestination(
    navController: NavController
) {
    composable(
        route = RegisterCampusConstant.ROUTE
    ) {
        val viewModel: RegisterCampusViewModel = hiltViewModel()

        val argument: RegisterCampusArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            RegisterCampusArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: RegisterCampusData = let {
            val campusList by viewModel.campusList.collectAsStateWithLifecycle()

            RegisterCampusData(
                campusList = campusList
            )
        }

        ErrorObserver(viewModel)
        RegisterCampusScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
