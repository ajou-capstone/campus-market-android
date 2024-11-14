package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Immutable
data class TradeInfoData(
    val tradeInfo: TradeInfo,
    val authorInfo: UserProfile,
    val userInfo: MyProfile
)
