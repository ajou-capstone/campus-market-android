package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.item

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.ItemQueryParameter
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SummarizedItemListRepository

class GetSummarizedLatestItemListUseCase @Inject constructor(
    private val summarizedItemListRepository: SummarizedItemListRepository
) {

    suspend fun getSummarizedLatestItemsUseCase(itemQueryParameter: ItemQueryParameter)
            : Result<MutableList<SummarizedItem>> {

        return summarizedItemListRepository.getSummarizedItemList(itemQueryParameter)
    }
}
