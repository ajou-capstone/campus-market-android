package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@Immutable
data class TradePostArgument(
    val state: TradePostState,
    val event: EventFlow<TradePostEvent>,
    val intent: (TradePostIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface TradePostState {
    data object Init : TradePostState
    data object Loading : TradePostState
}

sealed interface TradePostEvent

sealed interface TradePostIntent {
    data class PostNewTrade(
        val title: String,
        val description: String,
        val price: Int,
        val category: String,
        val imageList: List<GalleryImage>?
    ) : TradePostIntent
}