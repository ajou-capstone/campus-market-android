package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification

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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.notification.DeleteAllNotificationUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.notification.DeleteNotificationByIdUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.notification.GetNotificationHistoryUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNotificationHistoryUseCase: GetNotificationHistoryUseCase,
    private val deleteNotificationByIdUseCase: DeleteNotificationByIdUseCase,
    private val deleteAllNotificationUseCase: DeleteAllNotificationUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<NotificationState> =
        MutableStateFlow(NotificationState.Init)
    val state: StateFlow<NotificationState> = _state.asStateFlow()

    private val _event: MutableEventFlow<NotificationEvent> = MutableEventFlow()
    val event: EventFlow<NotificationEvent> = _event.asEventFlow()

    private val _notifications: MutableStateFlow<PagingData<UserNotification>> =
        MutableStateFlow(PagingData.empty())
    val notifications: StateFlow<PagingData<UserNotification>> =
        _notifications.asStateFlow()

    fun onIntent(intent: NotificationIntent) {
        when (intent) {
            is NotificationIntent.RefreshData -> {
                launch {
                    getNotificationHistory()
                }
            }

            is NotificationIntent.DeleteNotificationById -> {
                launch {
                    deleteNotificationById(intent.notificationId)
                    getNotificationHistory()
                }
            }

            is NotificationIntent.DeleteAllNotification -> {
                launch {
                    deleteAllNotification()
                    getNotificationHistory()
                }
            }
        }
    }

    init {
        launch {
            getNotificationHistory()
        }
    }

    private suspend fun getNotificationHistory() {
        getNotificationHistoryUseCase().cachedIn(viewModelScope)
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
                _notifications.value = it
            }
    }

    private suspend fun deleteNotificationById(notificationId: Long) {
        deleteNotificationByIdUseCase(notificationId).onFailure { exception ->
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

    private suspend fun deleteAllNotification() {
        deleteAllNotificationUseCase().onFailure { exception ->
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
