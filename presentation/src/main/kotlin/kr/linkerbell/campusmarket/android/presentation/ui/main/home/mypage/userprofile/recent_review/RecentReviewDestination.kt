package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.recentReviewDestination(
    navController: NavController
) {
    composable(
        route = RecentReviewConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(RecentReviewConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: RecentReviewViewModel = hiltViewModel()

        val argument: RecentReviewArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            RecentReviewArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: RecentReviewData = let {
            val recentReviews = viewModel.recentReviews.collectAsLazyPagingItems()

            RecentReviewData(
                recentReviews = recentReviews
            )
        }

        ErrorObserver(viewModel)
        RecentReviewScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
