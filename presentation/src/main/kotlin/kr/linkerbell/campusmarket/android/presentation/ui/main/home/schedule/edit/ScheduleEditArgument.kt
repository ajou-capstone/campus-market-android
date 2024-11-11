package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.edit

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ScheduleEditArgument(
    val state: ScheduleEditState,
    val event: EventFlow<ScheduleEditEvent>,
    val intent: (ScheduleEditIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ScheduleEditState {
    data object Init : ScheduleEditState
}

sealed interface ScheduleEditEvent

sealed interface ScheduleEditIntent
