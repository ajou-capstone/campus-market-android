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
                    val s3UrlsForImages = intent.imageList?.map { image ->
                        buildPreSignedUrlForImage(image)
                    } ?: emptyList()

                    val thumbnailUrl = s3UrlsForImages.firstOrNull() ?: ""
                    val imageUrls = s3UrlsForImages.drop(1)

                    postNewTrade(
                        intent.title,
                        intent.description,
                        intent.price,
                        intent.category,
                        thumbnailUrl = thumbnailUrl,
                        s3UrlsForImage = imageUrls
                    )
                }
            }
        }
    }

    private suspend fun buildPreSignedUrlForImage(image: GalleryImage?): String {

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
        }
        return s3Url
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
            title = title,
            description = description,
            price = price,
            category = category,
            thumbnail = thumbnailUrl,
            images = s3UrlsForImage
        ).onSuccess {
            _state.value = TradePostState.Init
        }.onFailure {
            _state.value = TradePostState.Init
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
