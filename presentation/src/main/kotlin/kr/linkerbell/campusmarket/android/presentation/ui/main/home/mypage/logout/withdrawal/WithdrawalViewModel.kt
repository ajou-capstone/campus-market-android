package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal

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
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history.DeleteAllSearchHistoryUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.WithdrawUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val withdrawUseCase: WithdrawUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val deleteAllSearchHistoryUseCase: DeleteAllSearchHistoryUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<WithdrawalState> = MutableStateFlow(WithdrawalState.Init)
    val state: StateFlow<WithdrawalState> = _state.asStateFlow()

    private val _event: MutableEventFlow<WithdrawalEvent> = MutableEventFlow()
    val event: EventFlow<WithdrawalEvent> = _event.asEventFlow()

    private val _myProfile: MutableStateFlow<MyProfile> = MutableStateFlow(MyProfile.empty)
    val myProfile: StateFlow<MyProfile> = _myProfile.asStateFlow()

    init {
        launch {
            getMyProfile()
        }
    }

    fun onIntent(intent: WithdrawalIntent) {
        when (intent) {
            is WithdrawalIntent.Withdrawal -> {
                launch {
                    userWithdraw()
                }
            }
        }
    }

    private suspend fun userWithdraw() {
        withdrawUseCase().onSuccess {
            deleteAllSearchHistoryUseCase()
            _state.value = WithdrawalState.Init
        }.onFailure { exception ->
            _state.value = WithdrawalState.Init
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

    private suspend fun getMyProfile() {
        getMyProfileUseCase().onSuccess {
            _state.value = WithdrawalState.Init
            _myProfile.value = it
        }.onFailure { exception ->
            _state.value = WithdrawalState.Init
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
