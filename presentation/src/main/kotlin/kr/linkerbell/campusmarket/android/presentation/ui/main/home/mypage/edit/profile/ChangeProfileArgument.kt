package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.profile

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@Immutable
data class ChangeProfileArgument(
    val state: ChangeProfileState,
    val event: EventFlow<ChangeProfileEvent>,
    val intent: (ChangeProfileIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ChangeProfileState {
    data object Init : ChangeProfileState
    data object Loading : ChangeProfileState
}

sealed interface ChangeProfileEvent {
    sealed interface SetProfile : ChangeProfileEvent {
        data object Success : SetProfile
    }
}

sealed interface ChangeProfileIntent {
    data object RefreshProfile : ChangeProfileIntent
    data class OnChangeNickname(val nickname: String) : ChangeProfileIntent
    data class OnChangeProfileImage(val image: GalleryImage?) : ChangeProfileIntent
}
