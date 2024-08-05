package kr.linkerbell.boardlink.android.presentation.ui.main

import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.domain.usecase.nonfeature.authentication.token.GetTokenRefreshFailEventFlowUseCase
import kr.linkerbell.boardlink.android.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTokenRefreshFailEventFlowUseCase: GetTokenRefreshFailEventFlowUseCase
) : BaseViewModel() {

    val refreshFailEvent: EventFlow<Unit> = getTokenRefreshFailEventFlowUseCase()

}
