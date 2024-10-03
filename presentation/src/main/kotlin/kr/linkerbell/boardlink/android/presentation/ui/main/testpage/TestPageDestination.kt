package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.boardlink.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.TestPageDestination(
    navController: NavController
) {
    composable(
        route = TestPageConstant.ROUTE,
        arguments = listOf(
            navArgument(TestPageConstant.ROUTE) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val viewModel: TestPageViewModel = hiltViewModel()

        val argument: TestPageArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            TestPageArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: TestPageData = let {
            val randomUserProfile by viewModel.userProfile.collectAsStateWithLifecycle()

            TestPageData(
                randomUserProfile = randomUserProfile,
                currentSemester = "2024-2" // 기본 값
            )
        }

        ErrorObserver(viewModel)
        TestPageScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
