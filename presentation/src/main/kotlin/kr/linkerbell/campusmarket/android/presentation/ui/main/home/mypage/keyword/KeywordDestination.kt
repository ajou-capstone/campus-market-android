package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.keyword

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.keywordDestination(
    navController: NavController
) {
    composable(
        route = KeywordConstant.ROUTE,
    ) {
        val viewModel: KeywordViewModel = hiltViewModel()

        val argument: KeywordArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            KeywordArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: KeywordData = let {
            val myKeyword by viewModel.myKeywords.collectAsStateWithLifecycle()

            KeywordData(
                myKeywords = myKeyword
            )
        }

        ErrorObserver(viewModel)
        KeywordScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
