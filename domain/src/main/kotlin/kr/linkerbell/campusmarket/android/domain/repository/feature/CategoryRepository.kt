package kr.linkerbell.campusmarket.android.domain.repository.feature

import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList

interface CategoryRepository {

    suspend fun getCategoryList(): Result<CategoryList>
}
