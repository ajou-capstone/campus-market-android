package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetRecentTradeListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetUserReviewUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetUserProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserReviewUseCase: GetUserReviewUseCase,
    private val getRecentTradeListUseCase: GetRecentTradeListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<UserProfileState> =
        MutableStateFlow(UserProfileState.Init)
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    private val _event: MutableEventFlow<UserProfileEvent> = MutableEventFlow()
    val event: EventFlow<UserProfileEvent> = _event.asEventFlow()

    private val _userProfile: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile.empty)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _recentReviews: MutableStateFlow<PagingData<UserReview>> =
        MutableStateFlow(PagingData.empty())
    val recentReviews: StateFlow<PagingData<UserReview>> = _recentReviews.asStateFlow()

    private val _recentTrades: MutableStateFlow<PagingData<RecentTrade>> =
        MutableStateFlow(PagingData.empty())
    val recentTrades: StateFlow<PagingData<RecentTrade>> = _recentTrades.asStateFlow()

    private val _userId: MutableStateFlow<Long> = MutableStateFlow(-1)

    init {
        launch {
            _userId.value = savedStateHandle.get<Long>(UserProfileConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L
            updateUserProfile(_userId.value)
        }
    }

    fun onIntent(intent: UserProfileIntent) {
        when(intent){
            is UserProfileIntent.RefreshUserProfile -> {
                launch {
                    updateUserProfile(_userId.value)
                }
            }
        }
    }

    private suspend fun updateUserProfile(userId: Long) {
        getOtherUserProfile(userId)
        getOtherUserReviews(userId)
        getOtherUserTradeHistory(userId)
    }

    private suspend fun getOtherUserProfile(userId: Long) {
        getUserProfileUseCase(userId).onSuccess {
            _state.value = UserProfileState.Init
            _userProfile.value = it
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _userProfile.value = UserProfile.empty
        }
    }

    private suspend fun getOtherUserReviews(userId: Long) {
        getUserReviewUseCase(userId)
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
                _recentReviews.value = it
            }
    }

    private suspend fun getOtherUserTradeHistory(userId: Long) {
        getRecentTradeListUseCase(userId, type = "sales")
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
                _recentTrades.value = it
            }
    }
}
