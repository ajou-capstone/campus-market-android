package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetInquiryListUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class InquiryViewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getInquiryListUseCase: GetInquiryListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<InquiryViewState> = MutableStateFlow(InquiryViewState.Init)
    val state: StateFlow<InquiryViewState> = _state.asStateFlow()

    private val _event: MutableEventFlow<InquiryViewEvent> = MutableEventFlow()
    val event: EventFlow<InquiryViewEvent> = _event.asEventFlow()

    private val _inquiryList: MutableStateFlow<PagingData<UserInquiry>> =
        MutableStateFlow(PagingData.empty())
    val inquiryList: StateFlow<PagingData<UserInquiry>> = _inquiryList.asStateFlow()

    fun onIntent(intent: InquiryViewIntent) {

    }

    init {
        launch {
            getInquiryList()
        }
    }

    private suspend fun getInquiryList() {
        getInquiryListUseCase()
            .cachedIn(viewModelScope)
            .catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _inquiryList.value = it
            }
    }
}
