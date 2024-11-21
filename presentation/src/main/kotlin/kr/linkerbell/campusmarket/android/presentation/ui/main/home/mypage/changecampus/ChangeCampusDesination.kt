package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.changecampus

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.changeCampusDestination(
    navController: NavController
) {
    composable(
        route = ChangeCampusConstant.ROUTE
    ) {
        val viewModel: ChangeCampusViewModel = hiltViewModel()

        val argument: ChangeCampusArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ChangeCampusArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ChangeCampusData = let {
            val campusList by viewModel.campusList.collectAsStateWithLifecycle()

            ChangeCampusData(
                campusList = campusList
            )
        }

        ErrorObserver(viewModel)
        ChangeCampusScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
