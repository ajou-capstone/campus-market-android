package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.item

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ItemReportArgument(
    val state: ItemReportState,
    val event: EventFlow<ItemReportEvent>,
    val intent: (ItemReportIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ItemReportState {
    data object Init : ItemReportState
    data object Loading : ItemReportState
}

sealed interface ItemReportEvent {
    sealed interface PostInquiry : ItemReportEvent {
        data object Success : PostInquiry
        data object Failure : PostInquiry
    }
}

sealed interface ItemReportIntent {
    data class PostItemReport(
        val category: String,
        val description: String
    ) : ItemReportIntent
}
