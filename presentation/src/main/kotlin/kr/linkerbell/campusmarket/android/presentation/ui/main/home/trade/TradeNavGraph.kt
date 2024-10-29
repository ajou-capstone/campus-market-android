package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradesearchpage.tradeSearchDestination

fun NavGraphBuilder.tradeNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = TradeConstant.ROUTE,
        route = TradeConstant.ROUTE
    ) {
        tradeSearchDestination(navController = navController)

    }
}
