package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.likes

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
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetMyLikesUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade.RecentTradeConstant

@HiltViewModel
class MyLikesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyLikesUseCase: GetMyLikesUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<MyLikesState> = MutableStateFlow(MyLikesState.Init)
    val state: StateFlow<MyLikesState> = _state.asStateFlow()

    private val _event: MutableEventFlow<MyLikesEvent> = MutableEventFlow()
    val event: EventFlow<MyLikesEvent> = _event.asEventFlow()

    private val _myLikes: MutableStateFlow<PagingData<SummarizedTrade>> =
        MutableStateFlow(PagingData.empty())
    val myLikes: StateFlow<PagingData<SummarizedTrade>> =
        _myLikes.asStateFlow()

    fun onIntent(intent: MyLikesIntent) {

    }

    init {
        launch {
            getMyLikes()
        }
    }

    private suspend fun getMyLikes() {
        getMyLikesUseCase().cachedIn(viewModelScope)
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
                _myLikes.value = it
            }
    }
}
