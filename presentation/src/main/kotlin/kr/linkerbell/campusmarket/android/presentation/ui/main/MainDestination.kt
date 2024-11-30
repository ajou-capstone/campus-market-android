package kr.linkerbell.campusmarket.android.presentation.ui.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat.chatDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.homeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.change_profile.changeProfileDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.changecampus.changeCampusDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.info.inquiryInfoDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.inquiryDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.post.inquiryPostDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view.inquiryViewDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.keyword.keywordDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.likes.myLikesDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.logoutDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal.withdrawalDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating.ratingDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent_review.myRecentReviewDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent_trade.myRecentTradeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review.recentReviewDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade.recentTradeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.userProfileDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare.scheduleCompareDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.tradeInfoDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.post.tradePostDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.result.tradeSearchResultDestination
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
    userProfileDestination(navController = navController)
    recentTradeDestination(navController = navController)
    recentReviewDestination(navController = navController)
    inquiryDestination(navController = navController)
    inquiryPostDestination(navController = navController)

    chatDestination(navController = navController)

    scheduleCompareDestination(navController = navController)

    changeCampusDestination(navController = navController)
    logoutDestination(navController = navController)
    withdrawalDestination(navController = navController)
    myRecentTradeDestination(navController = navController)
    myRecentReviewDestination(navController = navController)
    inquiryViewDestination(navController = navController)
    inquiryInfoDestination(navController = navController)
    myLikesDestination(navController = navController)
    changeProfileDestination(navController = navController)
    keywordDestination(navController = navController)
}
