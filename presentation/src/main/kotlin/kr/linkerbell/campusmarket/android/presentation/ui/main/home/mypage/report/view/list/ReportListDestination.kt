package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.view.list

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.reportListDestination(
    navController: NavController
) {
    composable(
        route = ReportListConstant.ROUTE,
    ) {
        val viewModel: ReportListViewModel = hiltViewModel()

        val argument: ReportListArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ReportListArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ReportListViewData = let {
            val inquiryList = viewModel.inquiryList.collectAsLazyPagingItems()

            ReportListViewData(
                userReportList = inquiryList
            )
        }

        ErrorObserver(viewModel)
        ReportListScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
