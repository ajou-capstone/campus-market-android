package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile

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

fun NavGraphBuilder.userProfileDestination(
    navController: NavController
) {
    composable(
        route = UserProfileConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(UserProfileConstant.ROUTE_ARGUMENT_USER_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: UserProfileViewModel = hiltViewModel()

        val argument: UserProfileArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            UserProfileArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: UserProfileData = let {
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
            val recentReviews = viewModel.recentReviews.collectAsLazyPagingItems()
            val recentTrades = viewModel.recentTrades.collectAsLazyPagingItems()

            UserProfileData(
                userProfile = userProfile,
                recentReviews = recentReviews,
                recentTrades = recentTrades
            )
        }

        ErrorObserver(viewModel)
        UserProfileScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
