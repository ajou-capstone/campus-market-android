package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.likes

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.myLikesDestination(
    navController: NavController
) {
    composable(
        route = MyLikesConstant.ROUTE,
    ) {
        val viewModel: MyLikesViewModel = hiltViewModel()

        val argument: MyLikesArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            MyLikesArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: MyLikesData = let {
            val myLikes = viewModel.myLikes.collectAsLazyPagingItems()

            MyLikesData(
                myLikes = myLikes
            )
        }

        ErrorObserver(viewModel)
        MyLikesScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
