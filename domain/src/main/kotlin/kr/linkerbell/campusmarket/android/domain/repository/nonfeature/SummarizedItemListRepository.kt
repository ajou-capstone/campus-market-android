package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.ItemQueryParameter
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem

interface SummarizedItemListRepository {

    suspend fun getSummarizedItemList(itemQueryParameter: ItemQueryParameter)
            : Result<MutableList<SummarizedItem>>
}
