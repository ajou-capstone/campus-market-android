package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage
import kotlin.coroutines.CoroutineContext

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

sealed interface TradePostEvent {
    data class NavigateToTrade(val tradeId: Long) : TradePostEvent
    data class FetchOriginalContents(val tradeContents: TradeContents) : TradePostEvent
    data object PatchOrPostFailed : TradePostEvent
    data object NoImageDetected : TradePostEvent
}

sealed interface TradePostIntent {
    data class PostOrPatchTrade(
        val title: String,
        val description: String,
        val price: Int,
        val category: String,
        val originalImageList: List<String>,
        val imageList: List<GalleryImage>
    ) : TradePostIntent
}
