package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.keyword

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.DeleteKeywordUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetMyKeywordListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.PostNewKeywordUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class KeywordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyKeywordListUseCase: GetMyKeywordListUseCase,
    private val postNewKeywordUseCase: PostNewKeywordUseCase,
    private val deleteKeywordUseCase: DeleteKeywordUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<KeywordState> = MutableStateFlow(KeywordState.Init)
    val state: StateFlow<KeywordState> = _state.asStateFlow()

    private val _event: MutableEventFlow<KeywordEvent> = MutableEventFlow()
    val event: EventFlow<KeywordEvent> = _event.asEventFlow()

    private val _myKeywords: MutableStateFlow<List<Keyword>> = MutableStateFlow(emptyList())
    val myKeywords: StateFlow<List<Keyword>> = _myKeywords.asStateFlow()

    fun onIntent(intent: KeywordIntent) {
        when (intent) {
            is KeywordIntent.RefreshKeyword -> {
                launch {
                    getMyKeywords()
                }
            }

            is KeywordIntent.PostKeyword -> {
                launch {
                    postNewKeyword(intent.keyword)
                }
            }

            is KeywordIntent.DeleteKeyword -> {
                launch {
                    deleteKeyword(intent.keywordId)
                }
            }
        }
    }

    init {
        launch {
            getMyKeywords()
        }
    }

    private suspend fun getMyKeywords() {
        getMyKeywordListUseCase().onSuccess {
            _myKeywords.value = it
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _myKeywords.value = emptyList()
        }
    }

    private suspend fun postNewKeyword(keywordName: String) {

        if (_myKeywords.value.find { it.keyword == keywordName } == null) {
            postNewKeywordUseCase(keywordName).onSuccess {
                _event.emit(KeywordEvent.NewKeywordPosted.Success)
            }.onFailure { exception ->
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
            _event.emit(KeywordEvent.NewKeywordPosted.Duplicated)
        }
    }

    private suspend fun deleteKeyword(keywordId: Long) {
        deleteKeywordUseCase(keywordId).onSuccess {
            _event.emit(KeywordEvent.KeywordDeleted.Success)
        }.onFailure { exception ->
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
