package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.report.item

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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ItemReportCategoryList
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.GetItemReportCategoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.PostItemReportUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ItemReportViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getItemReportCategoryListUseCase: GetItemReportCategoryListUseCase,
    private val postItemReportUseCase: PostItemReportUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ItemReportState> = MutableStateFlow(ItemReportState.Init)
    val state: StateFlow<ItemReportState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ItemReportEvent> = MutableEventFlow()
    val event: EventFlow<ItemReportEvent> = _event.asEventFlow()

    private val _itemReportCategoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(InquiryCategoryList.empty.categoryList)
    val itemReportCategoryList = _itemReportCategoryList.asStateFlow()

    val itemId: Long by lazy {
        savedStateHandle.get<Long>(ItemReportConstant.ROUTE_ARGUMENT_ITEM_ID) ?: -1L
    }

    init {
        launch {
            getItemReportCategoryList()
        }
    }

    fun onIntent(intent: ItemReportIntent) {
        when (intent) {
            is ItemReportIntent.PostItemReport -> {
                launch {
                    postReport(itemId, intent.category, intent.description)
                }
            }
        }
    }

    private suspend fun postReport(itemId: Long, category: String, description: String) {
        postItemReportUseCase(itemId, category, description).onSuccess {
            _event.emit(ItemReportEvent.PostInquiry.Success)
        }.onFailure { exception ->
            _event.emit(ItemReportEvent.PostInquiry.Failure)
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

    private suspend fun getItemReportCategoryList() {
        getItemReportCategoryListUseCase().onSuccess {
            _itemReportCategoryList.value = it.categoryList
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _itemReportCategoryList.value = ItemReportCategoryList.empty.categoryList
        }
    }
}
