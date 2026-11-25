package fr.uha.hassenforder.easy.cyclewall.ui

import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Converter {

    fun convert (datetime : LocalDateTime?, default : String = "") : String {
        return datetime?.let {
            if (datetime.hour == 0 && datetime.minute == 0) {
                it.format(DateTimeFormatter.ofPattern("dd MM yyyy"))
            } else {
                it.format(DateTimeFormatter.ofPattern("dd MM yyyy HH:mm"))
            }
        } ?: default
    }

    private fun fullConvert (datetime : LocalDateTime) : String {
        return datetime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    private fun everyYearConvert (datetime : LocalDateTime) : String {
        return datetime.format(DateTimeFormatter.ofPattern("dd MMM"))
    }

    private fun everyMonthConvert (datetime : LocalDateTime) : String {
        return datetime.format(DateTimeFormatter.ofPattern("dd"))
    }

    private fun everyWeekConvert (datetime : LocalDateTime) : String {
        return datetime.format(DateTimeFormatter.ofPattern("DD'"))
    }

    fun convert (datetime : LocalDateTime?, rhythm : CycleRhythm, default : String = "") : String {
        if (datetime == null) return ""
        return when (rhythm) {
        CycleRhythm.NO -> fullConvert(datetime)
        CycleRhythm.EVERY_YEAR -> everyYearConvert(datetime)
        CycleRhythm.EVERY_MONTH -> everyMonthConvert(datetime)
        CycleRhythm.EVERY_WEEK -> everyWeekConvert(datetime)
        CycleRhythm.ONE_DAY -> fullConvert(datetime)
        }
    }

}
