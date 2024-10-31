package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class RegisterCampusArgument(
    val state: RegisterCampusState,
    val event: EventFlow<RegisterCampusEvent>,
    val intent: (RegisterCampusIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RegisterCampusState {
    data object Init : RegisterCampusState
}

sealed interface RegisterCampusEvent {
    sealed interface SetCampus : RegisterCampusEvent {
        data object Success : SetCampus
    }
}

sealed interface RegisterCampusIntent {
    data class OnConfirm(val id: Long) : RegisterCampusIntent
}
