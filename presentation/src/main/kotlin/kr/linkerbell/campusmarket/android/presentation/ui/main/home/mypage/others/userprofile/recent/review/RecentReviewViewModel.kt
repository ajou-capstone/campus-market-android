package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetReviewsToMeUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class RecentReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getReviewsToMeUseCase: GetReviewsToMeUseCase,
) : BaseViewModel() {

    private val _state: MutableStateFlow<RecentReviewState> =
        MutableStateFlow(RecentReviewState.Init)
    val state: StateFlow<RecentReviewState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RecentReviewEvent> = MutableEventFlow()
    val event: EventFlow<RecentReviewEvent> = _event.asEventFlow()

    private val _recentReviews: MutableStateFlow<PagingData<UserReview>> =
        MutableStateFlow(PagingData.empty())
    val recentReviews: StateFlow<PagingData<UserReview>> = _recentReviews.asStateFlow()

    init {
        launch {
            val userId =
                savedStateHandle.get<Long>(RecentReviewConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L
            getOtherUserReviews(userId)
        }
    }

    fun onIntent(intent: RecentReviewIntent) {

    }

    private suspend fun getOtherUserReviews(userId: Long) {
        getReviewsToMeUseCase(userId)
            .cachedIn(viewModelScope)
            .catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _recentReviews.value = it
            }
    }
}
