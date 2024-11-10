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

    private var _itemId: String = savedStateHandle["itemId"] ?: ""

    init {
        launch {
            if (_itemId != "") {
                getOriginalTradeInfo(_itemId.toLong())
            }
            getCategoryList()
        }
    }

    fun onIntent(intent: TradePostIntent) {
        when (intent) {
            is TradePostIntent.PostOrPatchTrade -> {
                launch {
                    var s3UrlsForImages =
                        listOf(_originalTradeContents.value.thumbnail) + _originalTradeContents.value.images
                    val newImages = intent.imageList?.map { image ->
                        buildPreSignedUrlForImage(image)
                    } ?: emptyList()

                    s3UrlsForImages = (s3UrlsForImages + newImages).filter { it.isNotBlank() }

                    val thumbnailUrl = s3UrlsForImages.firstOrNull() ?: ""
                    val imageUrls = s3UrlsForImages.drop(1)

                    val tradeContent = TradeContents(
                        intent.title,
                        intent.description,
                        intent.price,
                        intent.category,
                        thumbnail = thumbnailUrl,
                        images = imageUrls
                    )

                    if (_itemId != "") {
                        _itemId = patchTradeContents(tradeContent)
                    } else {
                        _itemId = postTradeContents(
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

    private suspend fun postTradeContents(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnailUrl: String,
        s3UrlsForImage: List<String>
    ): String {
        _state.value = TradePostState.Loading
        postTradeContentsUseCase(
            title = title,
            description = description,
            price = price,
            category = category,
            thumbnail = thumbnailUrl,
            images = s3UrlsForImage
        ).onSuccess {
            _state.value = TradePostState.Init
            return it.toString()
        }.onFailure {
            _state.value = TradePostState.Init
        }
        return ""
    }

    private suspend fun patchTradeContents(
        tradeContents: TradeContents
    ): String {
        _state.value = TradePostState.Loading
        patchTradeContentsUseCase(
            tradeContents, _itemId.toLong()
        ).onSuccess {
            _state.value = TradePostState.Init
            return it.toString()
        }.onFailure {
            _state.value = TradePostState.Init
        }
        return ""
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
        }.onFailure {
            _state.value = TradePostState.Init
            _originalTradeContents.value = TradeContents.empty
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
