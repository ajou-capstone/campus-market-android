package kr.linkerbell.boardlink.android.presentation.ui.main.common.gallery

import androidx.compose.runtime.Immutable
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.presentation.model.gallery.GalleryFolder
import kotlin.coroutines.CoroutineContext

@Immutable
data class GalleryArgument(
    val state: GalleryState,
    val event: EventFlow<GalleryEvent>,
    val intent: (GalleryIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface GalleryState {
    data object Init : GalleryState
}

sealed interface GalleryEvent

sealed interface GalleryIntent {
    data object OnGrantPermission : GalleryIntent
    data class OnChangeFolder(
        val folder: GalleryFolder
    ) : GalleryIntent
}
