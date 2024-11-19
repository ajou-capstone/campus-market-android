package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.zip
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.MakeRoomUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.DeleteLikedItemUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.DeleteTradeInfoUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetTradeInfoUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.PostLikedItemUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetUserProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import javax.inject.Inject

@HiltViewModel
class TradeInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTradeInfoUseCase: GetTradeInfoUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val postLikedItemUseCase: PostLikedItemUseCase,
    private val deleteLikedItemUseCase: DeleteLikedItemUseCase,
    private val deleteTradeInfoUseCase: DeleteTradeInfoUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val makeRoomUseCase: MakeRoomUseCase
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

    private val itemId: Long by lazy {
        savedStateHandle.get<Long>(TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID) ?: -1L
    }

    init {
        launch {
            getTradeInfo()
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

            is TradeInfoIntent.OnTradeStart -> {
                makeRoom()
            }

            is TradeInfoIntent.RefreshNewTrades -> {
                launch { getTradeInfo() }
            }
        }
    }

    private fun changeLikeStatus() {
        if (tradeInfo.value.isLiked) {
            _tradeInfo.value = tradeInfo.value.copy(
                isLiked = !tradeInfo.value.isLiked,
                likeCount = tradeInfo.value.likeCount - 1
            )
            launch {
                deleteLikedItemUseCase(
                    itemId = _tradeInfo.value.itemId
                ).onFailure { exception ->
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
        } else {
            _tradeInfo.value = tradeInfo.value.copy(
                isLiked = !tradeInfo.value.isLiked,
                likeCount = tradeInfo.value.likeCount + 1
            )
            launch {
                postLikedItemUseCase(
                    itemId = tradeInfo.value.itemId
                ).onFailure { exception ->
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
    }

    private fun deleteTradeInfo() {
        _state.value = TradeInfoState.Loading
        launch {
            deleteTradeInfoUseCase(
                itemId = tradeInfo.value.itemId
            ).onSuccess {
                _state.value = TradeInfoState.Init
            }.onFailure { exception ->
                _state.value = TradeInfoState.Init
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

    private fun makeRoom() {
        launch {
            makeRoomUseCase(
                userId = userInfo.value.id,
                tradeId = itemId
            ).onSuccess {
                _state.value = TradeInfoState.Init
                _event.emit(TradeInfoEvent.NavigateToChatRoom(id = it.id))
            }.onFailure { exception ->
                _state.value = TradeInfoState.Init
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

    private suspend fun getTradeInfo() {
        _state.value = TradeInfoState.Loading
        zip(
            { getMyProfileUseCase() },
            { getTradeInfoUseCase(itemId) }
        ).mapCatching { (myProfile, tradeInfo) ->
            _userInfo.value = myProfile
            _tradeInfo.value = tradeInfo

            getUserProfileUseCase(tradeInfo.userId).getOrThrow()
        }.onSuccess {
            _state.value = TradeInfoState.Init
            _authorInfo.value = it
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
