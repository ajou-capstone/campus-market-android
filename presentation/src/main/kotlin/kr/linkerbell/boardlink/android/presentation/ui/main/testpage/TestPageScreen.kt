package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.math.roundToInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile
import kr.linkerbell.boardlink.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData.CalenderBlock
import kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData.DayOfTheWeek
import kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData.UserCalender

@Composable
fun TestPageScreen(
    navController: NavController,
    argument: TestPageArgument,
    data: TestPageData,
) {

    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    runCatching {
        data.randomUserProfile
    }.onSuccess { randomUserProfile ->
        Scaffold(
            topBar = {
                TestPageTopBar(randomUserProfile.fullName, data.currentSemester)
            },
            bottomBar = {
                TestPageBottomBar()
            },
            content = { paddingValues ->
                //Main content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF111111))
                )
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Spacer(Modifier.padding(top = 20.dp))
                    val grades = listOf(3.5, 3.7, 4.0, 3.95, 3.7)
                    TestPageGradeCalculator(grades)
                }
            }
        )
    }.onFailure { exception ->
        Text("Fail to load random user profile : $exception")
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Composable
fun TestPageTopBar(userName: String, semester: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Color(0xFF111111))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 5.dp)
            )
            {
                Text(text = userName, color = Color(0xFFEFEFEF))
                Text(
                    text = semester,
                    style = TextStyle(
                        color = Color(0xFFEFEFEF),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }

            Box() {

                Row() {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add block icon",
                        tint = Color(0xFFEFEFEF),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings icon",
                        tint = Color(0xFFEFEFEF),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "List icon",
                        tint = Color(0xFFEFEFEF),
                        modifier = Modifier.size(28.dp)
                    )
                }

            }
        }


    }

}

@Composable
fun TestPageBottomBar() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Color(0xFF1F1F1F))
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text("홈", color = Color.White, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text("메뉴", color = Color.White, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Calender icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text("시간표", color = Color.White, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text("지도", color = Color.White, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Account icon",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Text("프로필", color = Color.White, fontSize = 10.sp)
            }


        }


    }

}


val borderStyle = BorderStroke(1.dp, Color(0xFF2A2A2A))
val commonStyle = Modifier
    .border(
        width = 1.dp,
        color = Color(0xFF202020),
        shape = RoundedCornerShape(8.dp)
    )
    .background(
        color = Color(0xFF111111),
        shape = RoundedCornerShape(8.dp)
    )

@Composable
fun TestPageTimeTable(blockList: MutableList<CalenderBlock>) {

    Box(
        modifier = commonStyle.padding(8.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .border(borderStyle)
        ) {

            val cellModifier = Modifier
                .weight(1f)
                .fillMaxWidth()
            // 시간
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .border(borderStyle)
            ) {
                TextCell(text = "", cellModifier)
                for (hour in 9..18) {
                    TextCell(text = hour.toString(), cellModifier)
                }
            }

            //요일 별 column
            for (dayOfTheWeek in DayOfTheWeek.entries) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                ) {
                    TextCell(
                        text = dayOfTheWeek.toString(),
                        Modifier
                            .fillMaxWidth()
                            .weight(2f)
                    ) //MON

                    for (block in blockList) {
                        if (block.dayOfTheWeek == dayOfTheWeek) {
                            BlockCell(block.isAvailable, cellModifier) //Colored or not
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun TextCell(text: String, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(borderStyle)
    ) {
        Text(text = text, fontSize = 14.sp, color = Color(0xFF6F6F6F))
    }
}

@Composable
fun BlockCell(isColored: Boolean, modifier: Modifier) {
    Box(
        modifier = modifier
            .background(if (isColored) Color(0xFF6BB3A5) else Color.Transparent)
            .border(borderStyle)
    ) {
    }
}


@Composable
fun TestPageGradeCalculator(grades: List<Double>) {

    Column(
        modifier = commonStyle
            .padding(8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("학점 계산기", color = Color(0xFFEFEFEF), fontWeight = FontWeight.Bold)
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Score icon",
                tint = Color(0xFFEFEFEF),
                modifier = Modifier.size(16.dp)
            )
        }
        Row(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.width(130.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "평균 학점 ", color = Color(0xFFEFEFEF))
                Text(
                    text = "${(grades.average() * 100).roundToInt() / 100.0}",
                    color = Color(0xFFEF3D3A),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "/ 4.5", color = Color(0xFF5F5F5F))
            }

        }
    }
}


@Preview
@Composable
private fun TestPageScreenPreview() {
    TestPageScreen(
        navController = rememberNavController(),
        argument = TestPageArgument(
            state = TestPageState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO,
        ),
        data = TestPageData(RandomUserProfile.empty)
    )
}


@Preview
@Composable
private fun TimetableViewer() {

    val userCalender = UserCalender("홍길동", "2024-1")

    userCalender.addBlock(9, 0, DayOfTheWeek.MON)
    userCalender.addBlock(9, 1, DayOfTheWeek.MON)
    userCalender.addBlock(11, 0, DayOfTheWeek.TUE)
    userCalender.addBlock(11, 0, DayOfTheWeek.TUE)
    userCalender.addBlock(13, 1, DayOfTheWeek.TUE)
    userCalender.addBlock(14, 0, DayOfTheWeek.TUE)
    userCalender.addBlock(14, 1, DayOfTheWeek.TUE)

    TestPageTimeTable(userCalender.blockList)
}

@Preview
@Composable
private fun TestPageBottomBarViewer() {
    TestPageBottomBar()
}
