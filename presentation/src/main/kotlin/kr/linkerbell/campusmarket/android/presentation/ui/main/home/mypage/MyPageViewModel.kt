package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage

import androidx.lifecycle.SavedStateHandle
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProfileUseCase: GetProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<MyPageState> = MutableStateFlow(MyPageState.Init)
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<MyPageEvent> = MutableEventFlow()
    val event: EventFlow<MyPageEvent> = _event.asEventFlow()

    private val _profile: MutableStateFlow<Profile> = MutableStateFlow(Profile.empty)
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    init {
        launch {
            _state.value = MyPageState.Loading
            getProfileUseCase().onSuccess {
                _state.value = MyPageState.Init
                _profile.value = it
            }.onFailure { exception ->
                _state.value = MyPageState.Init
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

    fun onIntent(intent: MyPageIntent) {

    }
}
