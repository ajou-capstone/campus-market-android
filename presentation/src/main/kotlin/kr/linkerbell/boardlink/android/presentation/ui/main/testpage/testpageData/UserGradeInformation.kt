package kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData

import kotlin.math.roundToInt

class UserGradeInformation() {

    val grades = listOf(3.5, 3.7, 4.0, 3.95, 3.7)

    fun calculateMean(): Double {
        return (grades.average() * 100).roundToInt() / 100.0
    }

}
