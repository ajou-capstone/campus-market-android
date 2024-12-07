package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review

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

fun NavGraphBuilder.myRecentReviewDestination(
    navController: NavController
) {
    composable(
        route = MyRecentReviewConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(MyRecentReviewConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: MyRecentReviewViewModel = hiltViewModel()

        val argument: MyRecentReviewArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            MyRecentReviewArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: MyRecentReviewData = let {
            val reviewsToMe = viewModel.reviewsToMe.collectAsLazyPagingItems()
            val myReviews = viewModel.myReviews.collectAsLazyPagingItems()

            MyRecentReviewData(
                reviewsToMe = reviewsToMe,
                myReviews = myReviews
            )
        }

        ErrorObserver(viewModel)
        MyRecentReviewScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
