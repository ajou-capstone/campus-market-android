package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.RateUserUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class RatingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rateUserUseCase: RateUserUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RatingState> = MutableStateFlow(RatingState.Init)
    val state: StateFlow<RatingState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RatingEvent> = MutableEventFlow()
    val event: EventFlow<RatingEvent> = _event.asEventFlow()

    fun onIntent(intent: RatingIntent) {
        when (intent) {
            is RatingIntent.RateUser -> {
                launch {
                    rateUser(
                        intent.description,
                        intent.rating
                    )
                }
            }
        }
    }

    private suspend fun rateUser(
        description: String,
        rating: Int
    ) {
        rateUserUseCase(
            targetUserId = savedStateHandle.get<Long>(RatingConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L,
            itemId = savedStateHandle.get<Long>(RatingConstant.ROUTE_ARGUMENT_ITEM_ID) ?: -1L,
            description = description,
            rating = rating
        ).onSuccess {
            _state.value = RatingState.Init
            _event.emit(RatingEvent.RateSuccess)
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
        }
    }
}
