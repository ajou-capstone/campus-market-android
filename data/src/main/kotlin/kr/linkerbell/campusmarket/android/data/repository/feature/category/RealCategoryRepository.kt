package kr.linkerbell.campusmarket.android.data.repository.feature.category

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.CategoryApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList
import kr.linkerbell.campusmarket.android.domain.repository.feature.CategoryRepository

class RealCategoryRepository @Inject constructor(
    private val categoryApi: CategoryApi
) : CategoryRepository {
    override suspend fun getCategoryList(): Result<CategoryList> {
        return categoryApi.getCategoryList().toDomain()
    }
}
