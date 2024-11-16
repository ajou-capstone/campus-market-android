package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import kr.linkerbell.campusmarket.android.presentation.common.DOMAIN
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.chatDestination(
    navController: NavController
) {
    composable(
        route = ChatConstant.ROUTE_STRUCTURE,
        arguments = listOf(
            navArgument(ChatConstant.ROUTE_ARGUMENT_ROOM_ID) {
                type = NavType.LongType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${DOMAIN}/${ChatConstant.ROUTE_STRUCTURE}"
            }
        )
    ) {
        val viewModel: ChatViewModel = hiltViewModel()

        val argument: ChatArgument = let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            ChatArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                coroutineContext = viewModel.coroutineContext
            )
        }

        val data: ChatData = let {
            val messageList by viewModel.messageList.collectAsStateWithLifecycle()
            val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
            val myProfile by viewModel.myProfile.collectAsStateWithLifecycle()
            val room by viewModel.room.collectAsStateWithLifecycle()
            val trade by viewModel.trade.collectAsStateWithLifecycle()

            ChatData(
                messageList = messageList,
                userProfile = userProfile,
                myProfile = myProfile,
                room = room,
                trade = trade
            )
        }

        ErrorObserver(viewModel)
        ChatScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
