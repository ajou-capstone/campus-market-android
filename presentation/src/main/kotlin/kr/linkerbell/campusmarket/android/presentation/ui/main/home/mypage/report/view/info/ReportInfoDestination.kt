package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.view.info

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.reportInfoDestination(
    navController: NavController
) {
    composable(
        route = ReportInfoConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(ReportInfoConstant.ROUTE_ARGUMENT_QA_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: ReportInfoViewModel = hiltViewModel()

        val argument: ReportInfoArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ReportInfoArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ReportInfoData = let {
            val inquiryInfo by viewModel.reportInfo.collectAsStateWithLifecycle()

            ReportInfoData(
                reportInfo = inquiryInfo
            )
        }

        ErrorObserver(viewModel)
        ReportInfoScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
