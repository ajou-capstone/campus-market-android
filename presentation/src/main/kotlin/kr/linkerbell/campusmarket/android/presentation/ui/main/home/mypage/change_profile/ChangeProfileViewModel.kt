package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.change_profile

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
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.GetPreSignedUrlUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.UploadImageUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.SetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@HiltViewModel
class ChangeProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getPreSignedUrlUseCase: GetPreSignedUrlUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val setMyProfileUseCase: SetMyProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ChangeProfileState> =
        MutableStateFlow(ChangeProfileState.Init)
    val state: StateFlow<ChangeProfileState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ChangeProfileEvent> = MutableEventFlow()
    val event: EventFlow<ChangeProfileEvent> = _event.asEventFlow()

    private val _myProfile: MutableStateFlow<MyProfile> = MutableStateFlow(MyProfile.empty)
    val myProfile: StateFlow<MyProfile> = _myProfile.asStateFlow()

    fun onIntent(intent: ChangeProfileIntent) {
        when (intent) {
            is ChangeProfileIntent.OnChangeNickname -> {
                launch {
                    setNickname(nickname = intent.nickname)
                }
            }

            is ChangeProfileIntent.OnChangeProfileImage -> {
                launch {
                    setProfileImage(image = intent.image)
                }
            }

            is ChangeProfileIntent.RefreshProfile -> {
                launch {
                    getMyProfile()
                }
            }
        }
    }

    init {
        launch {
            getMyProfile()
        }
    }

    private suspend fun setNickname(nickname: String) {
        setMyProfileUseCase(
            nickname = nickname,
            profileImage = _myProfile.value.profileImage
        ).onSuccess {
            _state.value = ChangeProfileState.Init
            _event.emit(ChangeProfileEvent.SetProfile.Success)
        }.onFailure { exception ->
            _state.value = ChangeProfileState.Init
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

    private suspend fun setProfileImage(image: GalleryImage?) {
        if (image == null) {
            setMyProfileUseCase(
                nickname = _myProfile.value.nickname,
                profileImage = ""
            ).onSuccess {
                _state.value = ChangeProfileState.Init
                _event.emit(ChangeProfileEvent.SetProfile.Success)
            }.onFailure { exception ->
                _state.value = ChangeProfileState.Init
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }
        } else {
            getPreSignedUrlUseCase(
                fileName = image.name
            ).map { preSignedUrl ->
                uploadImageUseCase(
                    preSignedUrl = preSignedUrl.preSignedUrl,
                    imageUri = image.filePath
                ).map {
                    setMyProfileUseCase(
                        nickname = _myProfile.value.nickname,
                        profileImage = preSignedUrl.s3url
                    )
                }
            }.onSuccess {
                _state.value = ChangeProfileState.Init
                _event.emit(ChangeProfileEvent.SetProfile.Success)
            }.onFailure { exception ->
                _state.value = ChangeProfileState.Init
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }
            _state.value = ChangeProfileState.Loading
        }
    }

    private suspend fun getMyProfile() {
        getMyProfileUseCase().onSuccess {
            _state.value = ChangeProfileState.Init
            _myProfile.value = it
        }.onFailure { exception ->
            _state.value = ChangeProfileState.Init
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
