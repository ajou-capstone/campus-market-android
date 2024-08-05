package kr.linkerbell.boardlink.android.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.boardlink.android.presentation.R
import kr.linkerbell.boardlink.android.presentation.common.theme.BoardlinkTheme
import kr.linkerbell.boardlink.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.boardlink.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.boardlink.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.boardlink.android.presentation.common.view.DialogScreen
import kr.linkerbell.boardlink.android.presentation.ui.main.splash.SplashConstant

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    BoardlinkTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SplashConstant.ROUTE
        ) {
            mainDestination(navController)
        }

        ErrorObserver(viewModel)
        MainScreenRefreshFailDialog(navController, viewModel.refreshFailEvent)
    }
}

@Composable
fun MainScreenRefreshFailDialog(
    navController: NavHostController,
    refreshFailEvent: EventFlow<Unit>
) {
    var isInvalidTokenDialogShowing: Boolean by remember { mutableStateOf(false) }

    if (isInvalidTokenDialogShowing) {
        DialogScreen(
            isCancelable = false,
            title = stringResource(R.string.invalid_jwt_token_dialog_title),
            message = stringResource(R.string.invalid_jwt_token_dialog_content),
            onConfirm = {
                navController.safeNavigate(SplashConstant.ROUTE)
            },
            onDismissRequest = {
                isInvalidTokenDialogShowing = false
            }
        )
    }

    LaunchedEffectWithLifecycle(refreshFailEvent) {
        refreshFailEvent.eventObserve {
            isInvalidTokenDialogShowing = true
        }
    }
}
