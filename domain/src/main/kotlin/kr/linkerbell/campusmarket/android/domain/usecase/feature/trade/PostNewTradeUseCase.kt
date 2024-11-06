package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class PostNewTradeUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnail: String,
        images: List<String>
    ): Result<Long> {
        return tradeRepository.postNewTrade(
            title = title,
            description = description,
            price = price,
            category = category,
            thumbnail = thumbnail,
            images = images
        )
    }
}
