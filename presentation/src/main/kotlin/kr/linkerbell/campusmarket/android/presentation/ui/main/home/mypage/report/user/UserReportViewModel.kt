package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.user

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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.UserReportCategoryList
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.GetUserReportCategoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.PostUserReportUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class UserReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserReportCategoryListUseCase: GetUserReportCategoryListUseCase,
    private val postUserReportUseCase: PostUserReportUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<UserReportState> = MutableStateFlow(UserReportState.Init)
    val state: StateFlow<UserReportState> = _state.asStateFlow()

    private val _event: MutableEventFlow<UserReportEvent> = MutableEventFlow()
    val event: EventFlow<UserReportEvent> = _event.asEventFlow()

    private val _userReportCategoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(InquiryCategoryList.empty.categoryList)
    val userReportCategoryList = _userReportCategoryList.asStateFlow()

    val userId: Long by lazy {
        savedStateHandle.get<Long>(UserReportConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L
    }

    init {
        launch {
            getUserReportCategoryList()
        }
    }

    fun onIntent(intent: UserReportIntent) {
        when (intent) {
            is UserReportIntent.PostUserReport -> {
                launch {
                    postReport(userId, intent.category, intent.description)
                }
            }
        }
    }

    private suspend fun postReport(userId: Long, category: String, description: String) {
        postUserReportUseCase(userId, category, description).onSuccess {
            _event.emit(UserReportEvent.PostInquiry.Success)
        }.onFailure { exception ->
            _event.emit(UserReportEvent.PostInquiry.Failure)
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

    private suspend fun getUserReportCategoryList() {
        getUserReportCategoryListUseCase().onSuccess {
            _userReportCategoryList.value = it.categoryList
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _userReportCategoryList.value = UserReportCategoryList.empty.categoryList
        }
    }
}
