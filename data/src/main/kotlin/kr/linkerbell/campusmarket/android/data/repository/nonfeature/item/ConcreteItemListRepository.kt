package kr.linkerbell.campusmarket.android.data.repository.nonfeature.item

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SummarizedItemListRepository
import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.SummarizedItemsListApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.ItemQueryParameter
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem

class ConcreteItemListRepository @Inject constructor(
    private val api: SummarizedItemsListApi
) : SummarizedItemListRepository {

    override suspend fun getSummarizedItemList(itemQueryParameter: ItemQueryParameter)
            : Result<MutableList<SummarizedItem>> {
        return api.getSummarizedItem(itemQueryParameter).toDomain()
    }


}
