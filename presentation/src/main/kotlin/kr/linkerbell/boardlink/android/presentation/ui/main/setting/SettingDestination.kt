package kr.linkerbell.boardlink.android.presentation.ui.main.setting

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.boardlink.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.boardlink.android.presentation.ui.main.home.SettingConstant

fun NavGraphBuilder.Destination(
    navController: NavController
) {
    composable(
        route = SettingConstant.ROUTE_STRUCTURE_MAIN,
        arguments = listOf(
            navArgument(SettingConstant.ROUTE_ARGUMENT_SCREEN_MAIN) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val viewModel: SettingViewModel = hiltViewModel()

        val argument: SettingArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            SettingArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }


        ErrorObserver(viewModel)
        SettingScreen(
            navController = navController,
            argument = argument
        )
    }
}
