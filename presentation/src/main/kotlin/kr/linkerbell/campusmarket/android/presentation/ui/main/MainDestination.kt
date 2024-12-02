package kr.linkerbell.campusmarket.android.presentation.ui.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat.chatDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.homeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.campus.changeCampusDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.profile.changeProfileDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.logout.logoutDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.logout.withdrawal.withdrawalDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification.notificationDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.rating.ratingDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.inquiry.inquiryDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.item.itemReportDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.user.userReportDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.info.reportInfoDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.list.reportListDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.review.recentReviewDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.trade.recentTradeDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.userProfileDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.keyword.keywordDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.likes.myLikesDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review.myRecentReviewDestination
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade.myRecentTradeDestination
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

    chatDestination(navController = navController)

    scheduleCompareDestination(navController = navController)

    changeCampusDestination(navController = navController)
    logoutDestination(navController = navController)
    withdrawalDestination(navController = navController)
    myRecentTradeDestination(navController = navController)
    myRecentReviewDestination(navController = navController)
    myLikesDestination(navController = navController)
    changeProfileDestination(navController = navController)
    keywordDestination(navController = navController)
    notificationDestination(navController = navController)

    reportListDestination(navController = navController)
    reportInfoDestination(navController = navController)
    itemReportDestination(navController = navController)
    userReportDestination(navController = navController)
}