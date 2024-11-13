package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage

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
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<MyPageState> = MutableStateFlow(MyPageState.Init)
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<MyPageEvent> = MutableEventFlow()
    val event: EventFlow<MyPageEvent> = _event.asEventFlow()

    private val _myProfile: MutableStateFlow<MyProfile> = MutableStateFlow(MyProfile.empty)
    val myProfile: StateFlow<MyProfile> = _myProfile.asStateFlow()

    init {
        launch {
            getMyProfile()
        }
    }

    fun onIntent(intent: MyPageIntent) {
        when(intent){
            is MyPageIntent.RefreshData -> {
                launch{
                    getMyProfile()
                }
            }
        }
    }

    private suspend fun getMyProfile(){
        getMyProfileUseCase().onSuccess {
            _state.value = MyPageState.Init
            _myProfile.value = it
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
            _myProfile.value = MyProfile.empty
        }
    }
}
