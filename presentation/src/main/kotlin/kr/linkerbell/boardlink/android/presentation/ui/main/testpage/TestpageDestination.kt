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

fun NavGraphBuilder.testpageDestination(
    navController: NavController
) {
    composable(
        route = TestpageConstant.ROUTE,
        arguments = listOf(
            navArgument(TestpageConstant.ROUTE) {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        val viewModel: TestpageViewModel = hiltViewModel()

        val argument: testpageArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            testpageArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

//        val data: testpageData = let {
//            val initialData = viewModel.initialData
//
//            testpageData(
//                initialData = initialData
//            )
//        }

        ErrorObserver(viewModel)
        TestpageScreen(
            navController = navController,
            argument = argument,
        )
    }
}
