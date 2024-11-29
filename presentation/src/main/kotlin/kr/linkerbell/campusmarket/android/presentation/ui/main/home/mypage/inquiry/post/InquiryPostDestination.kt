package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.post

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.inquiryPostDestination(
    navController: NavController
) {
    composable(
        route = InquiryPostConstant.ROUTE
    ) {
        val viewModel: InquiryPostViewModel = hiltViewModel()

        val argument: InquiryPostArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            InquiryPostArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: InquiryPostData = let {
            val inquiryCategoryList = viewModel.inquiryCategoryList.collectAsStateWithLifecycle()

            InquiryPostData(
                inquiryCategoryList = inquiryCategoryList.value
            )
        }

        ErrorObserver(viewModel)
        InquiryPostScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
