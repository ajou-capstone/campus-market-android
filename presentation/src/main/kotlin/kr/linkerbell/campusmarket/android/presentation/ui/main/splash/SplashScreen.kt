package kr.linkerbell.campusmarket.android.presentation.ui.main.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.NonLoginConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.EntryConstant

@Composable
fun SplashScreen(
    navController: NavController,
    argument: SplashArgument,
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    fun navigateToEntry() {
        navController.navigate(EntryConstant.ROUTE) {
            popUpTo(SplashConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun navigateToNonLogin() {
        navController.navigate(NonLoginConstant.ROUTE) {
            popUpTo(SplashConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun login(event: SplashEvent.Login) {
        when (event) {
            is SplashEvent.Login.Success -> {
                navigateToEntry()
            }

            is SplashEvent.Login.Fail -> {
                navigateToNonLogin()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = Headline1
        )
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is SplashEvent.Login -> login(event)
            }
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        navController = rememberNavController(),
        argument = SplashArgument(
            state = SplashState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        )
    )
}
