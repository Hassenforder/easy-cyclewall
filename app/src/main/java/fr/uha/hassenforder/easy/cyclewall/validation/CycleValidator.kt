package fr.uha.hassenforder.easy.cyclewall.validation

import android.net.Uri
import androidx.compose.runtime.Immutable
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import fr.uha.hassenforder.easy.cyclewall.model.CycleScreen
import java.time.LocalDateTime

@Immutable
data class CycleValidationErrors(
    val title: Int?,
    val screen: Int?,
    val rhythm: Int?,
    val begin: Int?,
    val ending: Int?,
    val end: Int?,
    val duration : Int?,
    val wallpaper: Int?,
) {
    fun hasError() : Boolean {
        if (title != null) return true
        if (rhythm != null) return true
        if (begin != null) return true
        if (ending != null) return true
        if (end != null) return true
        if (duration != null) return true
        if (wallpaper != null) return true
        return false
    }
}

class CycleValidator(
    private val cycle: Cycle
) {

    private fun validateTitle(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 3 ->  R.string.value_too_short
            newValue.length > 12 ->  R.string.value_too_long
            else -> null
        }
    }

    private fun validateScreen(newValue: CycleScreen?) : Int? {
        return when {
            newValue == null ->  R.string.screen_must_set
            else -> null
        }
    }

    private fun validateRhythm(newValue: CycleRhythm?) : Int? {
        return when {
            newValue == null ->  R.string.rhythm_must_set
            else -> null
        }
    }

    private fun validateBegin(newValue: LocalDateTime) : Int? {
        return when {
            else -> null
        }
    }

    private fun validateEnding(end: LocalDateTime?, duration: Int?) : Int? {
        return when {
            end == null && duration == null ->  R.string.ending_must_set
            end != null && duration != null ->  R.string.ending_too_much_set
            else -> null // only one is set
        }
    }

    private fun validateEnd(newValue: LocalDateTime?) : Int? {
        return when {
            newValue == null ->  null  // not set but duration can be set
            newValue.isBefore(cycle.begin) -> R.string.end_after_begin
            else -> null
        }
    }

    private fun validateDuration(newValue: Int?) : Int? {
        return when {
            newValue == null ->  null // not set but end can be set
            newValue < 0 ->  R.string.duration_negative
            newValue == 0 ->  R.string.duration_zero
            else -> null
        }
    }

    @Suppress("SameReturnValue", "unused")
    private fun validateWallpaper(newValue: Uri?) : Int? {
        return null
    }

    fun validateAll(): CycleValidationErrors {
        return CycleValidationErrors(
            title = validateTitle(cycle.title),
            screen = validateScreen(cycle.screen),
            rhythm = validateRhythm(cycle.rhythm),
            begin = validateBegin(cycle.begin),
            ending = validateEnding(cycle.end, cycle.duration),
            end = validateEnd(cycle.end),
            duration = validateDuration(cycle.duration),
            wallpaper = validateWallpaper(cycle.wallpaper),
        )
    }

}
