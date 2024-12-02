package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.view.info

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ReportInfoArgument(
    val state: ReportInfoState,
    val event: EventFlow<ReportInfoEvent>,
    val intent: (ReportInfoIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ReportInfoState {
    data object Init : ReportInfoState
    data object Loading : ReportInfoState
}

sealed interface ReportInfoEvent

sealed interface ReportInfoIntent
