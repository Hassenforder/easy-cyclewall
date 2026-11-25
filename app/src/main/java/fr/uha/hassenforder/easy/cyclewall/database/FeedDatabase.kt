package fr.uha.hassenforder.easy.cyclewall.database

import android.net.Uri
import fr.uha.hassenforder.easy.android.database.model.ManagementState
import fr.uha.hassenforder.easy.cyclewall.model.*
import fr.uha.hassenforder.easy.cyclewall.R
import java.time.LocalDateTime
import java.util.*

class FeedDatabase (
    private val db : CycleWallDatabase
) {

    private fun build (year : Int, month : Int, dayOfMonth : Int) : LocalDateTime {
        return LocalDateTime.of(year, month, dayOfMonth, 0, 0)
    }

    private fun buildDrawingSeasons() : List<Cycle> {
        val cycles = listOf<Cycle> (
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "autumn",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 9, 23, 0, 0),
                end = LocalDateTime.of(2026, 12, 20, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_autumn)
            ),
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "winter",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 21, 0, 0),
                end = LocalDateTime.of(2027, 3, 19, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_winter)
            ),
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "spring",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 3, 20, 0, 0),
                end = LocalDateTime.of(2027, 6, 20, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_spring)
            ),
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "summer",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 6, 21, 0, 0),
                end = LocalDateTime.of(2027, 9, 22, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_summer)
            ),
        )
        return cycles
    }

    private fun buildDrawingChristmas() : List<Cycle> {
        val cycles = listOf<Cycle> (
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "winter",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 21, 0, 0),
                end = LocalDateTime.of(2027, 3, 19, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_winter)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "winter holidays",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 19, 0, 0),
                end = LocalDateTime.of(2027, 1, 4, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_winter_holiday)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "christmas days",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 24, 0, 0),
                end = LocalDateTime.of(2026, 12, 27, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_christmas)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "christmas",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 25, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.d_christmas)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "st Sylvestre",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 31, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.d_st_sylvestre)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "new year",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 1, 1, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.d_new_year)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "galette",
                screen = CycleScreen.SYSTEM,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 1, 5, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.d_galette)
            ),
        )
        return cycles
    }

    private fun buildDrawingYear() : List<Cycle> {
        val cycles = listOf<Cycle> ()
        return cycles
    }

    private fun buildPaintingSeasons() : List<Cycle> {
        val cycles = listOf<Cycle> (
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "autumn",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 9, 23, 0, 0),
                end = LocalDateTime.of(2026, 12, 20, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_autumn)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "winter",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 21, 0, 0),
                end = LocalDateTime.of(2027, 3, 19, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_winter)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "spring",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 3, 20, 0, 0),
                end = LocalDateTime.of(2027, 6, 20, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_spring)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "summer",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 6, 21, 0, 0),
                end = LocalDateTime.of(2027, 9, 22, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_summer)
            ),
        )
        return cycles
    }

    private suspend fun buildPaintingChristmas() : List<Cycle> {
        val cycles = listOf<Cycle> (
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "winter",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 21, 0, 0),
                end = LocalDateTime.of(2027, 3, 19, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_winter)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "winter holidays",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 19, 0, 0),
                end = LocalDateTime.of(2027, 1, 4, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_christmas)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "christmas days",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 24, 0, 0),
                end = LocalDateTime.of(2026, 12, 27, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.p_christmas)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "christmas",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 25, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.p_christmas)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "st Sylvestre",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2026, 12, 31, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.p_st_sylvestre)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "new year",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 1, 1, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.p_new_year)
            ),
            Cycle (cid = 0, managementState = ManagementState.VALID,
                title = "galette",
                screen = CycleScreen.LOCK,
                rhythm = CycleRhythm.EVERY_YEAR,
                begin = LocalDateTime.of(2027, 1, 5, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(R.mipmap.d_galette)
            ),
        )
        return cycles
    }

    private fun buildPaintingYear() : List<Cycle> {
        val cycles = listOf<Cycle> ()
        return cycles
    }

    private fun buildMonths1() : List<Cycle> {
        val cycles = listOf<Cycle> (
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "middle",
                screen = CycleScreen.SYSTEM_LOCK,
                rhythm = CycleRhythm.EVERY_MONTH,
                begin = LocalDateTime.of(2026, 7, 14, 0, 0),
                end = null,
                duration = 2,
                wallpaper = buildResourceUri(R.mipmap.d_galette)
            ),
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "crossDuration",
                screen = CycleScreen.SYSTEM_LOCK,
                rhythm = CycleRhythm.EVERY_MONTH,
                begin = LocalDateTime.of(2026, 9, 30, 0, 0),
                end = null,
                duration = 5,
                wallpaper = buildResourceUri(R.mipmap.d_fall_backward)
            ),
        )
        return cycles
    }

    private fun buildMonths2() : List<Cycle> {
        val cycles = listOf<Cycle> (
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "middle",
                screen = CycleScreen.SYSTEM_LOCK,
                rhythm = CycleRhythm.EVERY_MONTH,
                begin = LocalDateTime.of(2026, 10, 13, 0, 0),
                end = null,
                duration = 2,
                wallpaper = buildResourceUri(R.mipmap.d_first_april)
            ),
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "crossDay",
                screen = CycleScreen.SYSTEM_LOCK,
                rhythm = CycleRhythm.EVERY_MONTH,
                begin = LocalDateTime.of(2026, 3, 30, 0, 0),
                end = LocalDateTime.of(2026, 4, 3, 0, 0),
                duration = null,
                wallpaper = buildResourceUri(R.mipmap.d_spring_forward)
            ),
        )
        return cycles
    }

    private fun buildMonths3() : List<Cycle> {
        val cycles = (1 until 31).map {
            Cycle(
                cid = 0, managementState = ManagementState.VALID,
                title = "daily_${it}",
                screen = CycleScreen.SYSTEM_LOCK,
                rhythm = CycleRhythm.EVERY_MONTH,
                begin = LocalDateTime.of(2026, 12, it, 0, 0),
                end = null,
                duration = 1,
                wallpaper = buildResourceUri(dailyPictureIds[it])
            )
        }
        return cycles
    }

    private suspend fun createCycles(cycles : List<Cycle>) : List<Cycle> {
        val dao = db.getCycleDao()
        val created = cycles.map {
            val cid = dao.create(it)
            it.copy(cid = cid)
        }
        return created
    }

    private suspend fun createEvents(cycles : List<Cycle>) : List<CycleEvent> {
        val dao = db.getEventDao()
        val factory = EventFactory()
        val now = LocalDateTime.now()
        val created = cycles.map {
            val event = factory.buildEvent(it, now)
            if (event != null) {
                val eid = dao.create(event)
                event.copy(eid = eid)
            } else null
        }
        return created.filterNotNull()
    }

    @Suppress("unused")
    suspend fun populate(mode : Int, nextAlso : Boolean) {
        val cycles = when (mode) {
            1 -> buildDrawingSeasons()
            2 -> buildDrawingChristmas()
            3 -> buildDrawingYear()
            11 -> buildPaintingSeasons()
            12 -> buildPaintingChristmas()
            13 -> buildPaintingYear()
            21 -> buildMonths1()
            22 -> buildMonths2()
            23 -> buildMonths3()
            else -> emptyList<Cycle>()
        }
        val created = createCycles(cycles)
        if (nextAlso) {
            val allEvents = createEvents(created)
        }
    }

    fun clear() {
        db.clearAllTables()
    }

    companion object {
        private val rnd: Random = Random()

        private fun getRandomBetween(low: Int, high: Int): Int {
            return rnd.nextInt(high - low) + low
        }

        private fun getRandomBetween(low: Double, high: Double): Double {
            return rnd.nextDouble() * (high - low) + low
        }

        private val dailyPictureIds = intArrayOf (
            R.mipmap.j_0,
            R.mipmap.j_1,
            R.mipmap.j_2,
            R.mipmap.j_3,
            R.mipmap.j_4,
            R.mipmap.j_5,
            R.mipmap.j_6,
            R.mipmap.j_7,
            R.mipmap.j_8,
            R.mipmap.j_9,
            R.mipmap.j_10,
            R.mipmap.j_11,
            R.mipmap.j_12,
            R.mipmap.j_13,
            R.mipmap.j_14,
            R.mipmap.j_15,
            R.mipmap.j_16,
            R.mipmap.j_17,
            R.mipmap.j_18,
            R.mipmap.j_19,
            R.mipmap.j_20,
            R.mipmap.j_21,
            R.mipmap.j_22,
            R.mipmap.j_23,
            R.mipmap.j_24,
            R.mipmap.j_25,
            R.mipmap.j_26,
            R.mipmap.j_27,
            R.mipmap.j_28,
            R.mipmap.j_29,
            R.mipmap.j_30,
            R.mipmap.j_31,
        )

        private const val schemeName : String = "android.resource"
        private const val packageName : String = "fr.uha.hassenforder.easy.cyclewall"
        private fun buildResourceUri (id : Int) : Uri {
            return Uri.Builder()
                .scheme(schemeName)
                .authority(packageName)
                .path(id.toString())
                .build()
        }

    }

}