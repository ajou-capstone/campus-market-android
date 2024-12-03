package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.account.logout

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.system.exitProcess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen

@Composable
fun LogoutScreen(
    navController: NavController,
    argument: LogoutArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val context = LocalContext.current

    fun restartApp() {
        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let { intent ->
            context.startActivity(
                Intent.makeRestartActivityTask(intent.component)
            )
        }
        exitProcess(0)
    }

    DialogScreen(
        title = "로그아웃 하시겠습니까?",
        isCancelable = true,
        onConfirm = { argument.intent(LogoutIntent.LogOut) },
        onCancel = { navController.safeNavigateUp() },
        onDismissRequest = {
            navController.safeNavigateUp()
        }
    )

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is LogoutEvent.LogOutSuccess -> {
                    restartApp()
                }
            }
        }
    }
}

@Preview
@Composable
private fun LogoutScreenPreview() {
    LogoutScreen(
        navController = rememberNavController(),
        argument = LogoutArgument(
            state = LogoutState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        )
    )
}
