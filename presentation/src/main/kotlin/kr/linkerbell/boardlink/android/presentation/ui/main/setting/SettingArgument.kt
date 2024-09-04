package kr.linkerbell.boardlink.android.presentation.ui.main.setting

import javax.annotation.concurrent.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow

@Immutable
data class SettingArgument(
    val state : SettingState,
    val event: EventFlow<SettingEvent>,
    val intent : (SettingIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface SettingState{
    data object Init : SettingState
}

sealed interface SettingEvent{

}

sealed interface SettingIntent
