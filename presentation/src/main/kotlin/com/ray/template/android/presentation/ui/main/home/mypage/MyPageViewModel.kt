package com.ray.template.android.presentation.ui.main.home.mypage

import androidx.lifecycle.SavedStateHandle
import com.ray.template.android.common.util.coroutine.event.EventFlow
import com.ray.template.android.common.util.coroutine.event.MutableEventFlow
import com.ray.template.android.common.util.coroutine.event.asEventFlow
import com.ray.template.android.domain.model.nonfeature.error.ServerException
import com.ray.template.android.domain.model.nonfeature.user.Profile
import com.ray.template.android.domain.usecase.nonfeature.user.GetProfileUseCase
import com.ray.template.android.presentation.common.base.BaseViewModel
import com.ray.template.android.presentation.common.base.ErrorEvent
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
