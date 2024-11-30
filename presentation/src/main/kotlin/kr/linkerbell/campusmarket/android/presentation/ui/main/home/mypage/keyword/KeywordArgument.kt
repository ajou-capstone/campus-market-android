package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.keyword

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class KeywordArgument(
    val state: KeywordState,
    val event: EventFlow<KeywordEvent>,
    val intent: (KeywordIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface KeywordState {
    data object Init : KeywordState
    data object Loading : KeywordState
}

sealed interface KeywordEvent {
    sealed interface NewKeywordPosted : KeywordEvent {
        data object Success : NewKeywordPosted
        data object Duplicated: NewKeywordPosted
    }

    sealed interface KeywordDeleted : KeywordEvent {
        data object Success : KeywordDeleted
    }
}

sealed interface KeywordIntent {
    data object RefreshKeyword : KeywordIntent
    data class PostKeyword(val keyword: String) : KeywordIntent
    data class DeleteKeyword(val keywordId: Long) : KeywordIntent
}
