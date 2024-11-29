package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.inquiryViewDestination(
    navController: NavController
) {
    composable(
        route = InquiryViewConstant.ROUTE,
    ) {
        val viewModel: InquiryViewViewModel = hiltViewModel()

        val argument: InquiryViewArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            InquiryViewArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: InquiryViewData = let {
            val inquiryList = viewModel.inquiryList.collectAsLazyPagingItems()

            InquiryViewData(
                inquiryList = inquiryList
            )
        }

        ErrorObserver(viewModel)
        InquiryViewScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
