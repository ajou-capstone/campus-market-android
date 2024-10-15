package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.profile

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@Immutable
data class RegisterProfileArgument(
    val state: RegisterProfileState,
    val event: EventFlow<RegisterProfileEvent>,
    val intent: (RegisterProfileIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RegisterProfileState {
    data object Init : RegisterProfileState
    data object Loading : RegisterProfileState
}

sealed interface RegisterProfileEvent {
    sealed interface CheckNickname : RegisterProfileEvent {
        data object Failure : CheckNickname
    }

    sealed interface SetProfile : RegisterProfileEvent {
        data object Success : SetProfile
    }
}

sealed interface RegisterProfileIntent {
    data class OnConfirm(val nickname: String, val image: GalleryImage?) : RegisterProfileIntent
}
