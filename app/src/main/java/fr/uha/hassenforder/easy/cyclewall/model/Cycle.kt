package fr.uha.hassenforder.easy.cyclewall.model

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.uha.hassenforder.easy.android.database.model.ManagementState
import java.time.LocalDateTime

@Entity(tableName = "cycles")
@Immutable
data class Cycle(
    @PrimaryKey(autoGenerate = true)
    val cid : Long = 0,
    val managementState: ManagementState = ManagementState.CREATED,
    val title : String = "",
    val screen : CycleScreen,
    val rhythm : CycleRhythm = CycleRhythm.NO,
    val begin : LocalDateTime = LocalDateTime.now(),
    val end : LocalDateTime? = null,
    val duration : Int? = 0,
    val wallpaper : Uri? = null,
)
