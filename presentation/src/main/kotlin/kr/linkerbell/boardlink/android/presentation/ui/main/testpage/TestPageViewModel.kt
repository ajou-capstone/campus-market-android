package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.boardlink.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile
import kr.linkerbell.boardlink.android.domain.usecase.nonfeature.randomuserprofile.GetRandomUserProfileUseCase
import kr.linkerbell.boardlink.android.presentation.common.base.BaseViewModel
import kr.linkerbell.boardlink.android.presentation.common.base.ErrorEvent

@HiltViewModel
class TestPageViewModel @Inject constructor(
    private val getRandomUserProfileUseCase: GetRandomUserProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TestPageState> = MutableStateFlow(TestPageState.Init)
    val state: StateFlow<TestPageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TestPageEvent> = MutableEventFlow()
    val event: EventFlow<TestPageEvent> = _event.asEventFlow()

    private val _userProfile: MutableStateFlow<RandomUserProfile> =
        MutableStateFlow(RandomUserProfile.empty)
    val userProfile: StateFlow<RandomUserProfile> = _userProfile.asStateFlow()

    init {
        launch {
            _state.value = TestPageState.Loading
            getRandomUserProfileUseCase().onSuccess {
                _state.value = TestPageState.Init
                _userProfile.value = it
            }.onFailure { exception ->
                _state.value = TestPageState.Init
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

    fun onIntent(intent: TestPageIntent) {

    }

}
