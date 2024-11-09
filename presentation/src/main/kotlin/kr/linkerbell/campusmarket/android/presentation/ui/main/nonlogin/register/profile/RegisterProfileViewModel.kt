package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.profile

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
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.GetPreSignedUrlUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.UploadImageUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.SetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@HiltViewModel
class RegisterProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getPreSignedUrlUseCase: GetPreSignedUrlUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val setMyProfileUseCase: SetMyProfileUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RegisterProfileState> =
        MutableStateFlow(RegisterProfileState.Init)
    val state: StateFlow<RegisterProfileState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RegisterProfileEvent> = MutableEventFlow()
    val event: EventFlow<RegisterProfileEvent> = _event.asEventFlow()

    fun onIntent(intent: RegisterProfileIntent) {
        when (intent) {
            is RegisterProfileIntent.OnConfirm -> {
                setProfile(
                    nickname = intent.nickname,
                    image = intent.image
                )
            }
        }
    }

    private fun setProfile(
        nickname: String,
        image: GalleryImage?
    ) {
        launch {
            _state.value = RegisterProfileState.Loading

            if (image == null) {
                setMyProfileUseCase(
                    nickname = nickname,
                    profileImage = ""
                ).onSuccess {
                    _state.value = RegisterProfileState.Init
                    _event.emit(RegisterProfileEvent.SetProfile.Success)
                }.onFailure { exception ->
                    _state.value = RegisterProfileState.Init
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
                            nickname = nickname,
                            profileImage = preSignedUrl.s3url
                        )
                    }
                }.onSuccess {
                    _state.value = RegisterProfileState.Init
                    _event.emit(RegisterProfileEvent.SetProfile.Success)
                }.onFailure { exception ->
                    _state.value = RegisterProfileState.Init
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
    }
}
