package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.likes

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class MyLikesArgument(
    val state: MyLikesState,
    val event: EventFlow<MyLikesEvent>,
    val intent: (MyLikesIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface MyLikesState {
    data object Init : MyLikesState
    data object Loading : MyLikesState
}

sealed interface MyLikesEvent

sealed interface MyLikesIntent
