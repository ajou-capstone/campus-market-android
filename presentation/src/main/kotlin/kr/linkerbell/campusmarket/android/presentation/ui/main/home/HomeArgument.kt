package kr.linkerbell.campusmarket.android.presentation.ui.main.home

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class HomeArgument(
    val state: HomeState,
    val event: EventFlow<HomeEvent>,
    val intent: (HomeIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface HomeState {
    data object Init : HomeState
}

sealed interface HomeEvent {
    data object NeedSchedule : HomeEvent
}

sealed interface HomeIntent
