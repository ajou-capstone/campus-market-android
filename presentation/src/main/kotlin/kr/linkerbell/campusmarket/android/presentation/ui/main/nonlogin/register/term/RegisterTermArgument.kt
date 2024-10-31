package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class RegisterTermArgument(
    val state: RegisterTermState,
    val event: EventFlow<RegisterTermEvent>,
    val intent: (RegisterTermIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RegisterTermState {
    data object Init : RegisterTermState
    data object Loading : RegisterTermState
}

sealed interface RegisterTermEvent {
    sealed interface AgreeTerm : RegisterTermEvent {
        data object Success : AgreeTerm
    }
}

sealed interface RegisterTermIntent {
    data class OnConfirm(val checkedTermList: List<Long>) : RegisterTermIntent
}
