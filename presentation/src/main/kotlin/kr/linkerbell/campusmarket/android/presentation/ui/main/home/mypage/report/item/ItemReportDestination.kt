package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.item

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.itemReportDestination(
    navController: NavController
) {
    composable(
        route = ItemReportConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(ItemReportConstant.ROUTE_ARGUMENT_ITEM_ID) {
                type = NavType.LongType; defaultValue = -1L
            }
        )
    ) {
        val viewModel: ItemReportViewModel = hiltViewModel()

        val argument: ItemReportArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ItemReportArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ItemReportData = let {
            val itemReportCategoryList by viewModel.itemReportCategoryList.collectAsStateWithLifecycle()

            ItemReportData(
                itemReportCategoryList = itemReportCategoryList
            )
        }

        ErrorObserver(viewModel)
        ItemReportScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
