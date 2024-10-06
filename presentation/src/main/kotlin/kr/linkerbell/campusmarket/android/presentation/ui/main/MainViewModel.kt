package kr.linkerbell.campusmarket.android.presentation.ui.main

import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.token.GetTokenRefreshFailEventFlowUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTokenRefreshFailEventFlowUseCase: GetTokenRefreshFailEventFlowUseCase
) : BaseViewModel() {

    val refreshFailEvent: EventFlow<Unit> = getTokenRefreshFailEventFlowUseCase()

}
