package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kotlin.coroutines.CoroutineContext

@Immutable
data class RegisterEntryArgument(
    val state: RegisterEntryState,
    val event: EventFlow<RegisterEntryEvent>,
    val intent: (RegisterEntryIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RegisterEntryState {
    data object Init : RegisterEntryState
    data object Loading : RegisterEntryState
}

sealed interface RegisterEntryEvent {
    data object NeedTermAgreement : RegisterEntryEvent
    data object NeedUniversityRegistration : RegisterEntryEvent
    data object NeedCampusRegistration : RegisterEntryEvent
    data object NeedNickname : RegisterEntryEvent
    data object NoProblem : RegisterEntryEvent
}

sealed interface RegisterEntryIntent
