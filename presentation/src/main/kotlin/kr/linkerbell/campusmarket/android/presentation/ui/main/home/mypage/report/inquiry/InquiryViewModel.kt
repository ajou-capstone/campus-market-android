package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.inquiry

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.InquiryCategoryList
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.GetInquiryCategoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.PostInquiryUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getInquiryCategoryListUseCase: GetInquiryCategoryListUseCase,
    private val postInquiryUseCase: PostInquiryUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<InquiryState> = MutableStateFlow(InquiryState.Init)
    val state: StateFlow<InquiryState> = _state.asStateFlow()

    private val _event: MutableEventFlow<InquiryEvent> = MutableEventFlow()
    val event: EventFlow<InquiryEvent> = _event.asEventFlow()

    private val _inquiryCategoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(InquiryCategoryList.empty.categoryList)
    val inquiryCategoryList = _inquiryCategoryList.asStateFlow()

    fun onIntent(intent: InquiryIntent) {
        when (intent) {
            is InquiryIntent.PostInquiry -> {
                launch {
                    postInquiry(intent.title, intent.category, intent.description)
                }
            }
        }
    }

    init {
        launch {
            launch {
                getInquiryCategoryList()
            }
        }
    }

    private suspend fun postInquiry(title: String, category: String, description: String) {
        postInquiryUseCase(title, category, description).onSuccess {
            _event.emit(InquiryEvent.PostInquiry.Success)
        }.onFailure { exception ->
            _event.emit(InquiryEvent.PostInquiry.Failure)
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

    private suspend fun getInquiryCategoryList() {
        getInquiryCategoryListUseCase().onSuccess {
            _inquiryCategoryList.value = it.categoryList
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _inquiryCategoryList.value = InquiryCategoryList.empty.categoryList
        }
    }
}
