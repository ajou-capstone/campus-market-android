package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class UserProfileArgument(
    val state: UserProfileState,
    val event: EventFlow<UserProfileEvent>,
    val intent: (UserProfileIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface UserProfileState {
    data object Init : UserProfileState
    data object Loading : UserProfileState
}

sealed interface UserProfileEvent

sealed interface UserProfileIntent {
    data object RefreshUserProfile : UserProfileIntent
}
