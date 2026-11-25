package fr.uha.hassenforder.easy.cyclewall.model

import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.time.Duration


class EventFactory {
    fun isBefore (now : LocalDateTime, event : CycleEvent) : Boolean {
        return now.isBefore(event.begin)
    }

    fun isInside (now : LocalDateTime, event : CycleEvent) : Boolean {
        if (now.isBefore(event.begin)) return false
        if (now.isAfter(event.end)) return false
        return true
    }

    fun isAfter (now : LocalDateTime, event : CycleEvent) : Boolean {
        return now.isAfter(event.end)
    }

    private fun buildNextDay (rhythm : CycleRhythm, cycleReference : LocalDateTime, from : LocalDateTime) : LocalDateTime? {
        return when (rhythm) {
            CycleRhythm.EVERY_YEAR -> LocalDateTime.of(
                from.year,
                cycleReference.month,
                cycleReference.dayOfMonth,
                cycleReference.hour,
                cycleReference.minute,
            )
            CycleRhythm.EVERY_MONTH -> LocalDateTime.of(
                from.year,
                from.month,
                cycleReference.dayOfMonth,
                cycleReference.hour,
                cycleReference.minute,
            )
            CycleRhythm.EVERY_WEEK -> null
            CycleRhythm.ONE_DAY -> if (from.isAfter(from)) from else null
            CycleRhythm.NO -> null
        }
    }

    private fun buildNextSlot(rhythm : CycleRhythm, from : LocalDateTime) : LocalDateTime? {
        return when (rhythm) {
            CycleRhythm.EVERY_YEAR -> LocalDateTime.of(
                    from.year+1,
                    from.month,
                    from.dayOfMonth,
                    from.hour,
                    from.minute,
                )
            CycleRhythm.EVERY_MONTH -> LocalDateTime.of(
                from.year,
                from.month + 1,
                from.dayOfMonth,
                from.hour,
                from.minute,
            )
            CycleRhythm.EVERY_WEEK -> null
            CycleRhythm.ONE_DAY -> null
            CycleRhythm.NO -> null
        }
    }

    private fun correctCrossingRhythm (cycle : Cycle, day : LocalDateTime?) : LocalDateTime? {
        if (day == null) return null
        val end = cycle.end!!
        return when (cycle.rhythm) {
            CycleRhythm.EVERY_YEAR -> {
                val delta = end.year - cycle.begin.year
                LocalDateTime.of(
                    day.year + delta,
                    day.month,
                    day.dayOfMonth,
                    day.hour,
                    day.minute,
                )
            }
            CycleRhythm.EVERY_MONTH -> {
                val delta = end.month.value - cycle.begin.month.value
                LocalDateTime.of(
                    day.year,
                    day.month + delta.toLong(),
                    day.dayOfMonth,
                    day.hour,
                    day.minute,
                )
            }
            CycleRhythm.EVERY_WEEK -> null
            CycleRhythm.ONE_DAY -> null
            CycleRhythm.NO -> null
        }
    }

    private fun buildEndByDay (cycle : Cycle, begin : LocalDateTime) : LocalDateTime? {
        return correctCrossingRhythm(cycle, buildNextDay (cycle.rhythm, cycle.end!!, begin))
    }

    private fun buildEndByDuration (cycle : Cycle, begin : LocalDateTime) : LocalDateTime {
        return begin.plusDays(cycle.duration!!.toLong())
    }

    private fun buildEnd (cycle : Cycle, begin : LocalDateTime?) : LocalDateTime? {
        return when {
            begin == null -> null
            cycle.end != null && cycle.duration == null -> buildEndByDay (cycle, begin)
            cycle.end == null && cycle.duration != null -> buildEndByDuration (cycle, begin)
            else -> null
        }
    }

    private fun internalBuildEvent (cycle : Cycle, from : LocalDateTime) : CycleEvent? {
        val begin = buildNextDay(cycle.rhythm, cycle.begin, from)
        val end = buildEnd(cycle, begin)
        if (begin == null) return null
        if (end == null) return null
        return CycleEvent(cycle.cid, begin, end)
    }

    fun buildEvent (cycle : Cycle, from : LocalDateTime) : CycleEvent? {
        val event = internalBuildEvent(cycle, from)
        if (event == null) return null
        if (! isAfter(from, event)) return event
        val nextSlot = buildNextSlot(cycle.rhythm, from)
        if (nextSlot == null) return null
        val nextEvent = internalBuildEvent(cycle, nextSlot)
        if (nextEvent == null) return null
        if (! isAfter(from, nextEvent)) return nextEvent
        return null
    }

    fun bestEvent (events : List<FullEvent>, from : LocalDateTime) : FullEvent? {
        return events
            .mapNotNull { it.cycle?.let { c -> buildEvent(c, from) }?.let { e -> FullEvent(e, it.cycle) } }
            .filter { isInside(from, it.event) }
            .minByOrNull { it.event.end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() }
    }

    fun nextTimeInSeconds (events : List<FullEvent>, from : LocalDateTime) : Long? {
        val times = mutableListOf<Long>()
        events.filter { isInside(from, it.event) }
            .forEach {
                times.add(ChronoUnit.SECONDS.between(from, it.event.begin))
                times.add(ChronoUnit.SECONDS.between(from,it.event.end))
            }
        times.sort()
        return times.firstOrNull() { it >= 0 }
    }

}
