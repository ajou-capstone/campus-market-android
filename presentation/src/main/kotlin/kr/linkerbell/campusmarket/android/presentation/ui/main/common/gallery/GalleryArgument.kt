package kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryFolder

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
