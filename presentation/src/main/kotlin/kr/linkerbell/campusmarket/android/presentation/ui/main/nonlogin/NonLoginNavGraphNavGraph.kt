package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.entryDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login.LoginConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login.loginDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term.registerTermDestination

fun NavGraphBuilder.nonLoginNavGraphNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = LoginConstant.ROUTE,
        route = NonLoginConstant.ROUTE
    ) {
        loginDestination(navController = navController)
        entryDestination(navController = navController)
        registerTermDestination(navController = navController)
    }
}
