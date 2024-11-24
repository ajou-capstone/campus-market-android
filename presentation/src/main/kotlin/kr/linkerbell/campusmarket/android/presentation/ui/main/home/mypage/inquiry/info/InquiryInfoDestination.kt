package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.info

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.inquiryInfoDestination(
    navController: NavController
) {
    composable(
        route = InquiryInfoConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(InquiryInfoConstant.ROUTE_ARGUMENT_QA_ID) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        val viewModel: InquiryInfoViewModel = hiltViewModel()

        val argument: InquiryInfoArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            InquiryInfoArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: InquiryInfoData = let {
            val inquiryInfo by viewModel.inquiryInfo.collectAsStateWithLifecycle()

            InquiryInfoData(
                inquiryInfo = inquiryInfo
            )
        }

        ErrorObserver(viewModel)
        InquiryInfoScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
