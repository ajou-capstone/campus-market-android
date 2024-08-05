package kr.linkerbell.boardlink.android.presentation.ui.main.nonlogin.onboarding

import androidx.lifecycle.SavedStateHandle
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.boardlink.android.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state: MutableStateFlow<OnBoardingState> = MutableStateFlow(OnBoardingState.Init)
    val state: StateFlow<OnBoardingState> = _state.asStateFlow()

    private val _event: MutableEventFlow<OnBoardingEvent> = MutableEventFlow()
    val event: EventFlow<OnBoardingEvent> = _event.asEventFlow()

    fun onIntent(intent: OnBoardingIntent) {

    }
}
