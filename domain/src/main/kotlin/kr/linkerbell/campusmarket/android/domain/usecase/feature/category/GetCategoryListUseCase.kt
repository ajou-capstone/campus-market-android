package kr.linkerbell.campusmarket.android.domain.usecase.feature.category

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.category.CategoryList
import kr.linkerbell.campusmarket.android.domain.repository.feature.CategoryRepository

class GetCategoryListUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Result<CategoryList> {
        return categoryRepository.getCategoryList()
    }
}
