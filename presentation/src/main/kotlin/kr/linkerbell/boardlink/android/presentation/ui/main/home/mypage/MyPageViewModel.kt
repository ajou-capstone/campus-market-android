package kr.linkerbell.boardlink.android.presentation.ui.main.home.mypage

import androidx.lifecycle.SavedStateHandle
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.boardlink.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.boardlink.android.domain.usecase.nonfeature.user.GetProfileUseCase
import kr.linkerbell.boardlink.android.presentation.common.base.BaseViewModel
import kr.linkerbell.boardlink.android.presentation.common.base.ErrorEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile
import kr.linkerbell.boardlink.android.domain.usecase.nonfeature.randomuserprofile.GetRandomUserProfileUseCase

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProfileUseCase: GetProfileUseCase,
    private val getRandomUserProfileUseCase: GetRandomUserProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<MyPageState> = MutableStateFlow(MyPageState.Init)
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<MyPageEvent> = MutableEventFlow()
    val event: EventFlow<MyPageEvent> = _event.asEventFlow()

    private val _profile: MutableStateFlow<Profile> = MutableStateFlow(Profile.empty)
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    //Temp
    private val _randomUserProfile: MutableStateFlow<RandomUserProfile> =
        MutableStateFlow(RandomUserProfile.empty)
    val randomUserProfile: StateFlow<RandomUserProfile> = _randomUserProfile.asStateFlow()

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

            getRandomUserProfileUseCase().onSuccess {
                _state.value = MyPageState.Init
                _randomUserProfile.value = it
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
