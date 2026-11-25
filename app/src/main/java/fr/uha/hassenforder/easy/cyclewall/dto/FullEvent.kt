package fr.uha.hassenforder.easy.cyclewall.dto

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Relation
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleEvent

@Immutable
data class FullEvent (
    @Embedded
    val event: CycleEvent,

    @Relation(parentColumn = "eid", entityColumn = "cid")
    val cycle: Cycle?,

) {
    override fun toString(): String {
        if (cycle == null) return "FullEvent(${event.eid})"
        return "FullEvent(${event.eid} - ${cycle.title}"
    }
}