package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.view.list

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ReportListArgument(
    val state: ReportListState,
    val event: EventFlow<ReportListEvent>,
    val intent: (ReportListIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ReportListState {
    data object Init : ReportListState
    data object Loading : ReportListState
}

sealed interface ReportListEvent

sealed interface ReportListIntent{
    data object RefreshData: ReportListIntent
}
