package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ScheduleArgument(
    val state: ScheduleState,
    val event: EventFlow<ScheduleEvent>,
    val intent: (ScheduleIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ScheduleState {
    data object Init : ScheduleState
    data object Loading : ScheduleState
}

sealed interface ScheduleEvent

sealed interface ScheduleIntent
