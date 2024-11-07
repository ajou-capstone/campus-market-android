package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetCategoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.PostNewTradeUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.GetPreSignedUrlUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.UploadImageUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@HiltViewModel
class TradePostViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val postNewTradeUseCase: PostNewTradeUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getPreSignedUrlUseCase: GetPreSignedUrlUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradePostState> = MutableStateFlow(TradePostState.Init)
    val state: StateFlow<TradePostState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradePostEvent> = MutableEventFlow()
    val event: EventFlow<TradePostEvent> = _event.asEventFlow()

    private val _categoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(CategoryList.empty.categoryList)
    val categoryList: StateFlow<List<String>> = _categoryList.asStateFlow()

    init {
        launch {
            getCategoryList()
        }
    }

    fun onIntent(intent: TradePostIntent) {
        when (intent) {
            is TradePostIntent.PostNewTrade -> {
                launch {
                    val s3UrlsForImages = buildPreSignedUrlListForImages(intent.images)
                    val thumbnailImage = s3UrlsForImages[intent.thumbnailIndex]
                    postNewTrade(
                        intent.title,
                        intent.description,
                        intent.price,
                        intent.category,
                        thumbnailUrl = thumbnailImage,
                        s3UrlsForImage = s3UrlsForImages
                    )
                }
            }
        }
    }

    private suspend fun buildPreSignedUrlListForImages(images: List<GalleryImage>): List<String> {

        var urlList: List<String> = emptyList()
        var hasError = false

        images.forEach { image ->
            getPreSignedUrlUseCase(
                fileName = image.name
            ).map { preSignedUrl ->
                uploadImageUseCase(
                    preSignedUrl = preSignedUrl.presignedUrl,
                    imageUri = image.filePath
                )
                urlList = urlList + preSignedUrl.s3url
            }.onSuccess {
                _state.value = TradePostState.Init
                return urlList
            }.onFailure { exception ->
                _state.value = TradePostState.Init
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                        hasError = true
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                        hasError = true
                    }
                }
            }
        }
        return if (hasError) emptyList()
        else urlList
    }

    private suspend fun postNewTrade(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnailUrl: String,
        s3UrlsForImage: List<String>
    ) {
        _state.value = TradePostState.Loading

        postNewTradeUseCase(
            title,
            description,
            price,
            category,
            thumbnailUrl,
            s3UrlsForImage
        ).onSuccess {
            _state.value = TradePostState.Init
            val postedTradeId = it
            //TODO("받아 온 페이지(id = postedTradeId)로 이동")
        }.onFailure {
            _state.value = TradePostState.Init
            //TODO("실패 시 예외 처리")
        }
    }

    private suspend fun getCategoryList() {
        _state.value = TradePostState.Loading
        getCategoryListUseCase().onSuccess {
            _state.value = TradePostState.Init
            _categoryList.value = it.categoryList
        }.onFailure {
            _state.value = TradePostState.Init
            _categoryList.value = CategoryList.empty.categoryList
        }
    }

}
