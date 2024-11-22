package kr.linkerbell.campusmarket.android.presentation.ui.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat.chatDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.homeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.changecampus.changeCampusDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.logoutDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal.withdrawalDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare.scheduleCompareDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.tradeInfoDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post.tradePostDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating.ratingDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result.tradeSearchResultDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.tradeSearchDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.nonLoginNavGraphNavGraph
import kr.linkerbell.campusmarket.android.presentation.ui.main.splash.splashDestination

fun NavGraphBuilder.mainDestination(
    navController: NavController
) {
    splashDestination(navController = navController)
    nonLoginNavGraphNavGraph(navController = navController)
    homeDestination(navController = navController)

    tradeSearchDestination(navController = navController)
    tradeSearchResultDestination(navController = navController)
    tradePostDestination(navController = navController)
    tradeInfoDestination(navController = navController)

    ratingDestination(navController = navController)

    chatDestination(navController = navController)

    scheduleCompareDestination(navController = navController)

    changeCampusDestination(navController = navController)
    logoutDestination(navController = navController)
    withdrawalDestination(navController = navController)
}
