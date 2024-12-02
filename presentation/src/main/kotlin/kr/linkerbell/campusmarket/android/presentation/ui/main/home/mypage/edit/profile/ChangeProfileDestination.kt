package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.profile

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.changeProfileDestination(
    navController: NavController
) {
    composable(
        route = ChangeProfileConstant.ROUTE,
    ) {
        val viewModel: ChangeProfileViewModel = hiltViewModel()

        val argument: ChangeProfileArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ChangeProfileArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ChangeProfileData = let {
            val userProfile by viewModel.myProfile.collectAsStateWithLifecycle()

            ChangeProfileData(
                myProfile = userProfile
            )
        }

        ErrorObserver(viewModel)
        ChangeProfileScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
