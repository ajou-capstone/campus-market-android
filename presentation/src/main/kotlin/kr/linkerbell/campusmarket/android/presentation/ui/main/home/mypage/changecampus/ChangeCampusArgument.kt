package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.changecampus

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kotlin.coroutines.CoroutineContext

@Immutable
data class ChangeCampusArgument(
    val state: ChangeCampusState,
    val event: EventFlow<ChangeCampusEvent>,
    val intent: (ChangeCampusIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ChangeCampusState {
    data object Init : ChangeCampusState
    data object Loading : ChangeCampusState
}


sealed interface ChangeCampusEvent {
    sealed interface SetCampus : ChangeCampusEvent {
        data object Success : SetCampus
    }
}

sealed interface ChangeCampusIntent {
    data class OnConfirm(val id: Long) : ChangeCampusIntent
}
