package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ScheduleCompareArgument(
    val state: ScheduleCompareState,
    val event: EventFlow<ScheduleCompareEvent>,
    val intent: (ScheduleCompareIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ScheduleCompareState {
    data object Init : ScheduleCompareState
}

sealed interface ScheduleCompareEvent

sealed interface ScheduleCompareIntent
