package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.inquiry

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.inquiryDestination(
    navController: NavController
) {
    composable(
        route = InquiryConstant.ROUTE
    ) {
        val viewModel: InquiryViewModel = hiltViewModel()

        val argument: InquiryArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            InquiryArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: InquiryData = let {
            val inquiryCategoryList = viewModel.inquiryCategoryList.collectAsStateWithLifecycle()

            InquiryData(
                inquiryCategoryList = inquiryCategoryList.value
            )
        }

        ErrorObserver(viewModel)
        InquiryScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
