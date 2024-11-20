package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class RatingArgument(
    val state: RatingState,
    val event: EventFlow<RatingEvent>,
    val intent: (RatingIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RatingState {
    data object Init : RatingState
    data object Loading : RatingState
}

sealed interface RatingEvent{
    data object RateSuccess: RatingEvent
}

sealed interface RatingIntent {
    data class RateUser(
        val description: String,
        val rating: Int
    ) : RatingIntent
}
