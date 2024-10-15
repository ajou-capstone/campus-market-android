package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.HomeConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus.RegisterCampusConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.profile.RegisterProfileConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term.RegisterTermConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.university.RegisterUniversityConstant

@Composable
fun RegisterEntryScreen(
    navController: NavController,
    argument: RegisterEntryArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext
    val context = LocalContext.current

    fun navigateToRegisterTerm() {
        navController.navigate(RegisterTermConstant.ROUTE) {
            popUpTo(EntryConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun navigateToRegisterUniversity() {
        navController.navigate(RegisterUniversityConstant.ROUTE) {
            popUpTo(EntryConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun navigateToRegisterCampus() {
        navController.navigate(RegisterCampusConstant.ROUTE) {
            popUpTo(EntryConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun navigateToRegisterProfile() {
        navController.navigate(RegisterProfileConstant.ROUTE) {
            popUpTo(EntryConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun navigateToHome() {
        navController.navigate(HomeConstant.ROUTE) {
            popUpTo(EntryConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                RegisterEntryEvent.NeedTermAgreement -> {
                    navigateToRegisterTerm()
                }

                RegisterEntryEvent.NeedUniversityRegistration -> {
                    navigateToRegisterUniversity()
                }

                RegisterEntryEvent.NeedCampusRegistration -> {
                    navigateToRegisterCampus()
                }

                RegisterEntryEvent.NeedNickname -> {
                    navigateToRegisterProfile()
                }

                RegisterEntryEvent.NoProblem -> {
                    navigateToHome()
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterEntryScreenPreview() {
    RegisterEntryScreen(
        navController = rememberNavController(),
        argument = RegisterEntryArgument(
            state = RegisterEntryState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        )
    )
}
