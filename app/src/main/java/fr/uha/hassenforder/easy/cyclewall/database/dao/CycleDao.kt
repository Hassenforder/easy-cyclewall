package fr.uha.hassenforder.easy.cyclewall.database.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.android.database.model.ManagementState
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import fr.uha.hassenforder.easy.cyclewall.model.CycleScreen
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
abstract class CycleDao {

    enum class ScreenFilter {
        ALL,
        SYSTEM,
        LOCK,
    }

    sealed class CycleUpdate {
        data class Title(val cid : Long, val title: String) : CycleUpdate()
        data class Rhythm(val cid : Long, val rhythm: CycleRhythm) : CycleUpdate()
        data class Screen(val cid : Long, val screen: CycleScreen) : CycleUpdate()
        data class Begin(val cid : Long, val begin: LocalDateTime) : CycleUpdate()
        data class Ending(val cid : Long, val end: LocalDateTime?, val duration : Int?) : CycleUpdate()
        data class Wallpaper(val cid : Long, val wallpaper: Uri?) : CycleUpdate()
        data class Management(val cid : Long, val managementState : ManagementState) : CycleUpdate()
    }

    @Query("SELECT *  FROM cycles")
    abstract fun getAll(): Flow<List<Cycle>>

    @Transaction
    @Query("SELECT C.* FROM cycles AS C ORDER BY C.begin")
    abstract fun getAllWithDetails(): Flow<List<Cycle>>

    @Transaction
    @Query("""SELECT C.* 
            FROM cycles AS C 
            WHERE C.managementState = 'VALID' OR C.managementState = 'INVALID'
            ORDER BY C.begin """
    )
    abstract fun getFilteredWithDetails(): Flow<List<Cycle>>

    @RawQuery(observedEntities = [Cycle::class])
    protected abstract fun getAllCycles(query : SimpleSQLiteQuery): Flow<List<Cycle>>

    private fun buildSimpleQuery (
        whereClause : List<String>,
    ) : SimpleSQLiteQuery {
        val outputQuery = StringBuilder()
        outputQuery.append("SELECT C.* ")
        outputQuery.append("FROM cycles AS C ")
        if (whereClause.isNotEmpty()) {
            outputQuery.append(whereClause.joinToString(" AND ", prefix = "WHERE ", postfix = " "))
        }
        outputQuery.append("ORDER BY C.begin ")
        val simpleSQLiteQuery = SimpleSQLiteQuery(outputQuery.toString(), arrayOf<String>())
        return simpleSQLiteQuery
    }

    fun query (mode : ListMode, filter : ScreenFilter) : Flow<List<Cycle>> {
        val screen = when (filter) {
            ScreenFilter.ALL -> ""
            ScreenFilter.SYSTEM -> " ( C.screen = 'SYSTEM' OR C.screen = 'SYSTEM_LOCK' ) "
            ScreenFilter.LOCK -> " ( C.screen = 'LOCK' OR C.screen = 'SYSTEM_LOCK' ) "
        }
        val mode = when (mode) {
            ListMode.ALL -> ""
            ListMode.FILTERED -> " ( C.managementState = 'VALID' OR C.managementState = 'INVALID' ) "
        }
        val whereClause = mutableListOf<String>()
        if (screen.isNotEmpty()) whereClause.add(screen)
        if (mode.isNotEmpty()) whereClause.add(mode)
        return getAllCycles(buildSimpleQuery(whereClause))
    }

    @Transaction
    @Query("SELECT C.* FROM cycles AS C WHERE cid = :id ")
    abstract fun getById(id: Long?): Flow<Cycle?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun create(cycle: Cycle): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(cycle: Cycle): Long

    @Delete
    abstract suspend fun delete(cycle: Cycle)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Title)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Rhythm)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Screen)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Begin)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Ending)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Wallpaper)

    @Update(entity = Cycle::class)
    abstract suspend fun update(content : CycleUpdate.Management)

}
