package kr.linkerbell.campusmarket.android.data.repository.feature.category

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList
import kr.linkerbell.campusmarket.android.domain.repository.feature.CategoryRepository

class MockCategoryRepository @Inject constructor() : CategoryRepository {
    override suspend fun getCategoryList(): Result<CategoryList> {
        return Result.success(CategoryList.empty)
    }
}
