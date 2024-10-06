package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login.loginDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.onboarding.OnBoardingConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.onboarding.onBoardingDestination

fun NavGraphBuilder.nonLoginNavGraphNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = OnBoardingConstant.ROUTE,
        route = NonLoginConstant.ROUTE
    ) {
        onBoardingDestination(navController = navController)
        loginDestination(navController = navController)
    }
}
