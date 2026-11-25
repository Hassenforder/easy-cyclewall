package fr.uha.hassenforder.easy.cyclewall.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "events")
@Immutable
data class CycleEvent(
    @PrimaryKey(autoGenerate = true)
    val eid : Long = 0, // should be same as cycleId so only one event can live at a time
    val begin : LocalDateTime,
    val end : LocalDateTime,
) {
    @Ignore
    val duration : Long = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - begin.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
