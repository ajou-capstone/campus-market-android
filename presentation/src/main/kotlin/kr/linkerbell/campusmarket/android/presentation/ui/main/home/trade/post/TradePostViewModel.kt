package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetCategoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetTradeInfoUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.PatchTradeContentsUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.PostTradeContentsUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.GetPreSignedUrlUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.UploadImageUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage
import javax.inject.Inject

@HiltViewModel
class TradePostViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val postTradeContentsUseCase: PostTradeContentsUseCase,
    private val patchTradeContentsUseCase: PatchTradeContentsUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getPreSignedUrlUseCase: GetPreSignedUrlUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val getTradeInfoUseCase: GetTradeInfoUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradePostState> = MutableStateFlow(TradePostState.Init)
    val state: StateFlow<TradePostState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradePostEvent> = MutableEventFlow()
    val event: EventFlow<TradePostEvent> = _event.asEventFlow()

    private val _categoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(CategoryList.empty.categoryList)
    val categoryList: StateFlow<List<String>> = _categoryList.asStateFlow()

    private val _originalTradeContents: MutableStateFlow<TradeContents> =
        MutableStateFlow(TradeContents.empty)
    val originalTradeContents: StateFlow<TradeContents> = _originalTradeContents.asStateFlow()

    private val tradeId: Long by lazy {
        savedStateHandle.get<Long>(TradePostConstant.ROUTE_ARGUMENT_ITEM_ID) ?: -1L
    }

    init {
        launch {
            if (tradeId != -1L) {
                getOriginalTradeInfo(tradeId)
            }
            getCategoryList()
        }
    }

    fun onIntent(intent: TradePostIntent) {
        when (intent) {
            is TradePostIntent.PostOrPatchTrade -> {
                launch {
                    val s3UrlsForImages = buildImageList(intent.originalImageList, intent.imageList)

                    if (s3UrlsForImages.isEmpty()) {
                        _event.emit(TradePostEvent.NoImageDetected)
                    } else {
                        val tradeContent = TradeContents(
                            intent.title,
                            intent.description,
                            intent.price,
                            intent.category,
                            thumbnail = s3UrlsForImages.firstOrNull() ?: "",
                            images = s3UrlsForImages.drop(1)
                        )
                        if (tradeId != -1L) {
                            patchTradeContents(tradeContent)
                        } else {
                            postTradeContents(tradeContent)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getPreSignedUrlForImage(image: GalleryImage?): String {

        if (image == null) return ""

        var s3Url = ""

        getPreSignedUrlUseCase(
            fileName = image.name
        ).map { preSignedUrl ->
            uploadImageUseCase(
                preSignedUrl = preSignedUrl.preSignedUrl,
                imageUri = image.filePath
            )
            s3Url = preSignedUrl.s3url
        }.onSuccess {
            _state.value = TradePostState.Init
        }.onFailure { exception ->
            _state.value = TradePostState.Init
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _event.emit(TradePostEvent.PatchOrPostFailed)
        }
        return s3Url
    }

    private suspend fun buildImageList(
        originalImageList: List<String>,
        newImageList: List<GalleryImage>
    ): List<String> {
        val newS3Links = newImageList.map { image ->
            getPreSignedUrlForImage(image)
        }
        return (originalImageList + newS3Links).filter { it.isNotBlank() }
    }

    private suspend fun postTradeContents(tradeContents: TradeContents) {
        _state.value = TradePostState.Loading
        postTradeContentsUseCase(
            title = tradeContents.title,
            description = tradeContents.description,
            price = tradeContents.price,
            category = tradeContents.category,
            thumbnail = tradeContents.thumbnail,
            images = tradeContents.images
        ).onSuccess {
            _state.value = TradePostState.Init
            _event.emit(TradePostEvent.NavigateToTrade(tradeId = it))
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _event.emit(TradePostEvent.PatchOrPostFailed)
        }
    }

    private suspend fun patchTradeContents(tradeContents: TradeContents) {
        _state.value = TradePostState.Loading
        patchTradeContentsUseCase(
            tradeContents,
            tradeId
        ).onSuccess {
            _state.value = TradePostState.Init
            _event.emit(TradePostEvent.NavigateToTrade(tradeId = tradeId))
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _event.emit(TradePostEvent.PatchOrPostFailed)
        }
    }

    private suspend fun getOriginalTradeInfo(itemId: Long) {
        _state.value = TradePostState.Loading
        getTradeInfoUseCase(itemId).onSuccess {
            _state.value = TradePostState.Init
            _originalTradeContents.value = TradeContents(
                title = it.title,
                description = it.description,
                price = it.price,
                category = it.category,
                thumbnail = it.thumbnail,
                images = it.images
            )
            _event.emit(TradePostEvent.FetchOriginalContents(_originalTradeContents.value))
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _originalTradeContents.value = TradeContents.empty
        }
    }

    private suspend fun getCategoryList() {
        _state.value = TradePostState.Loading
        getCategoryListUseCase().onSuccess {
            _state.value = TradePostState.Init
            _categoryList.value = it.categoryList
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
