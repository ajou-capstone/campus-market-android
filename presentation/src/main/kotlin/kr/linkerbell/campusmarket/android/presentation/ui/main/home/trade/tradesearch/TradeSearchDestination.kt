package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradesearch

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.tradeSearchDestination(
    navController: NavController
) {
    composable(
        route = TradeSearchConstant.ROUTE,
    ) {
        TradeSearchScreen(
            navController = navController
        )
    }
}
