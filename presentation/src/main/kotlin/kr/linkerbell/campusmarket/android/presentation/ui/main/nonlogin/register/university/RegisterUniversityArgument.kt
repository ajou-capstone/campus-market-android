package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.university

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class RegisterUniversityArgument(
    val state: RegisterUniversityState,
    val event: EventFlow<RegisterUniversityEvent>,
    val intent: (RegisterUniversityIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RegisterUniversityState {
    data object Init : RegisterUniversityState
}

sealed interface RegisterUniversityEvent {
    sealed interface CheckEmail : RegisterUniversityEvent {
        data object Success : CheckEmail
        data object Failure : CheckEmail
    }

    sealed interface CheckVerifyCode : RegisterUniversityEvent {
        data object Success : CheckVerifyCode
        data object Failure : CheckVerifyCode
    }
}

sealed interface RegisterUniversityIntent {
    data class OnSendEmail(val email: String) : RegisterUniversityIntent
    data class OnConfirm(val verifyCode: String) : RegisterUniversityIntent
}
