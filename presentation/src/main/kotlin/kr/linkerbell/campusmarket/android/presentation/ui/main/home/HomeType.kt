package kr.linkerbell.campusmarket.android.presentation.ui.main.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.MyPageConstant
import kotlinx.parcelize.Parcelize
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.landingpage.LandingPageConstant

@Parcelize
sealed class HomeType(
    val route: String,
    @DrawableRes val iconRes: Int
) : Parcelable {

    @Parcelize
    data object MyPage : HomeType(
        route = MyPageConstant.ROUTE,
        iconRes = R.drawable.ic_account
    )

    @Parcelize
    data object LandingPage : HomeType(
        route = LandingPageConstant.ROUTE,
        iconRes = R.drawable.ic_menu
    )

    companion object {
        fun values(): List<HomeType> {
            return listOf(MyPage, LandingPage)
        }
    }
}
