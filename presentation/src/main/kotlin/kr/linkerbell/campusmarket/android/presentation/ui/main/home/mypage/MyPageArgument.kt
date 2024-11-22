package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class MyPageArgument(
    val state: MyPageState,
    val event: EventFlow<MyPageEvent>,
    val intent: (MyPageIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface MyPageState {
    data object Init : MyPageState
    data object Loading : MyPageState
}

sealed interface MyPageEvent

sealed interface MyPageIntent{
    data object RefreshData : MyPageIntent
}
