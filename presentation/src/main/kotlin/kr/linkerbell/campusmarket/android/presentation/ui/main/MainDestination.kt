package kr.linkerbell.campusmarket.android.presentation.ui.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.homeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.nonLoginNavGraphNavGraph
import kr.linkerbell.campusmarket.android.presentation.ui.main.splash.splashDestination

fun NavGraphBuilder.mainDestination(
    navController: NavController
) {
    splashDestination(navController = navController)
    nonLoginNavGraphNavGraph(navController = navController)
    homeDestination(navController = navController)
}
