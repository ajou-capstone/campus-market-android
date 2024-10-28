package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class LandingPageArgument(
    val state: LandingPageState,
    val event: EventFlow<LandingPageEvent>,
    val intent: (LandingPageIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface LandingPageState {
    data object Init : LandingPageState
}

sealed interface LandingPageEvent

sealed interface LandingPageIntent
