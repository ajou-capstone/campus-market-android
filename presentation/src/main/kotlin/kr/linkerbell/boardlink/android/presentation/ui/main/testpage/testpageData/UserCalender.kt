package kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData


class UserCalender(
    val userName: String,
    val semester: String
) {
    val blockList: MutableList<CalenderBlock> = mutableListOf()

    init {
        for (dayOfTheWeek in DayOfTheWeek.entries) {
            for (hr in 9..18) {
                blockList.add(CalenderBlock(hr, 0, dayOfTheWeek))
                blockList.add(CalenderBlock(hr, 1, dayOfTheWeek))
            }
        }

    }

    fun addBlock(hour: Int, half: Int, dayOfTheWeek: DayOfTheWeek) {
        for (block in blockList) {
            if (block.hour == hour && block.half == half && block.dayOfTheWeek == dayOfTheWeek) {
                block.isAvailable = true
            }
        }
    }

    fun deleteBlock(hour: Int, half: Int, dayOfTheWeek: DayOfTheWeek) {
        for (block in blockList) {
            if (block.hour == hour && block.half == half && block.dayOfTheWeek == dayOfTheWeek) {
                block.isAvailable = false
            }
        }
    }

}

enum class DayOfTheWeek {
    MON, TUE, WED, THU, FRI
}


class CalenderBlock(
    val hour: Int,
    val half: Int,
    val dayOfTheWeek: DayOfTheWeek,
    var isAvailable: Boolean = false
) {
    override fun toString(): String {
        return "${dayOfTheWeek} : hour = ${hour} / half = ${half} : ${isAvailable}"
    }
}
