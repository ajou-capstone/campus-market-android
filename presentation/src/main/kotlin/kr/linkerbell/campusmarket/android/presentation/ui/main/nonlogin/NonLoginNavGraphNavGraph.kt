package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.entryDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login.LoginConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login.loginDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus.registerCampusDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.profile.registerProfileDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term.registerTermDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.university.registerUniversityDestination

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
        registerUniversityDestination(navController = navController)
        registerCampusDestination(navController = navController)
        registerProfileDestination(navController = navController)
    }
}
