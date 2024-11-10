package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.DeleteLikedItemUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.DeleteTradeInfoUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetTradeInfoUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.PostLikedItemUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetUserProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class TradeInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTradeInfoUseCase: GetTradeInfoUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val postLikedItemUseCase: PostLikedItemUseCase,
    private val deleteLikedItemUseCase: DeleteLikedItemUseCase,
    private val deleteTradeInfoUseCase: DeleteTradeInfoUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradeInfoState> = MutableStateFlow(TradeInfoState.Init)
    val state: StateFlow<TradeInfoState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradeInfoEvent> = MutableEventFlow()
    val event: EventFlow<TradeInfoEvent> = _event.asEventFlow()

    private val _tradeInfo: MutableStateFlow<TradeInfo> = MutableStateFlow(TradeInfo.empty)
    val tradeInfo: StateFlow<TradeInfo> = _tradeInfo.asStateFlow()

    private val _authorInfo: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile.empty)
    val authorInfo: StateFlow<UserProfile> = _authorInfo.asStateFlow()

    private val _userInfo: MutableStateFlow<MyProfile> = MutableStateFlow(MyProfile.empty)
    val userInfo: StateFlow<MyProfile> = _userInfo.asStateFlow()

    private val itemId = savedStateHandle["itemId"] ?: "-1"

    init {
        launch {
            getTradeInfo(itemId.toLong())
            getUserInfo(_tradeInfo.value.userId)
            getMyInfo()
        }
    }

    fun onIntent(intent: TradeInfoIntent) {
        when (intent) {
            is TradeInfoIntent.LikeButtonClicked -> {
                changeLikeStatus()
            }

            is TradeInfoIntent.DeleteThisPost -> {
                deleteTradeInfo()
            }
        }
    }

    private suspend fun getMyInfo() {
        _state.value = TradeInfoState.Loading
        getMyProfileUseCase().onSuccess {
            _state.value = TradeInfoState.Init
            _userInfo.value = it
        }.onFailure {
            _state.value = TradeInfoState.Init
        }
    }

    private suspend fun getTradeInfo(itemId: Long) {
        _state.value = TradeInfoState.Loading
        getTradeInfoUseCase(itemId).onSuccess {
            _state.value = TradeInfoState.Init
            _tradeInfo.value = it
        }.onFailure {
            _state.value = TradeInfoState.Init
        }
    }

    private suspend fun getUserInfo(id: Long) {
        _state.value = TradeInfoState.Loading

        getUserProfileUseCase(id).onSuccess {
            _state.value = TradeInfoState.Init
            _authorInfo.value = it
        }.onFailure {
            _state.value = TradeInfoState.Init
        }
    }

    private fun changeLikeStatus() {
        if (_tradeInfo.value.isLiked) {
            _tradeInfo.value = _tradeInfo.value.copy(
                isLiked = !_tradeInfo.value.isLiked,
                likeCount = _tradeInfo.value.likeCount - 1
            )
            launch { deleteLikedItemUseCase(_tradeInfo.value.itemId) }
        } else {
            _tradeInfo.value = _tradeInfo.value.copy(
                isLiked = !_tradeInfo.value.isLiked,
                likeCount = _tradeInfo.value.likeCount + 1
            )
            launch { postLikedItemUseCase(_tradeInfo.value.itemId) }
        }
    }

    private fun deleteTradeInfo() {
        _state.value = TradeInfoState.Loading
        launch {
            deleteTradeInfoUseCase(_tradeInfo.value.itemId).onSuccess {
                _state.value = TradeInfoState.Init
            }.onFailure {
                _state.value = TradeInfoState.Init
            }
        }
    }
}
