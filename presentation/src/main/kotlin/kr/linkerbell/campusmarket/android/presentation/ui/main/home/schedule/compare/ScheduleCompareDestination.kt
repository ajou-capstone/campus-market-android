package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.scheduleCompareDestination(
    navController: NavController
) {
    composable(
        route = ScheduleCompareConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(ScheduleCompareConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
            }
        )
    ) {
        val viewModel: ScheduleCompareViewModel = hiltViewModel()

        val argument: ScheduleCompareArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ScheduleCompareArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ScheduleCompareData = let {
            val mySchedule by viewModel.mySchedule.collectAsStateWithLifecycle()
            val userSchedule by viewModel.userSchedule.collectAsStateWithLifecycle()

            ScheduleCompareData(
                mySchedule = mySchedule,
                userSchedule = userSchedule
            )
        }

        ErrorObserver(viewModel)
        ScheduleCompareScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
