package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.user

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.userReportDestination(
    navController: NavController
) {
    composable(
        route = UserReportConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(UserReportConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType; defaultValue = -1L
            }
        )
    ) {
        val viewModel: UserReportViewModel = hiltViewModel()

        val argument: UserReportArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            UserReportArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: UserReportData = let {
            val userReportCategoryList by viewModel.userReportCategoryList.collectAsStateWithLifecycle()

            UserReportData(
                userReportCategoryList = userReportCategoryList
            )
        }

        ErrorObserver(viewModel)
        UserReportScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
