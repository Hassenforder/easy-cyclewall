package fr.uha.hassenforder.easy.cyclewall.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.ScreenFilter
import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleEvent
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EventDao {

    @Query("SELECT * FROM events")
    abstract fun getAll(): Flow<List<CycleEvent>>

    @Transaction
    @Query("""SELECT E.* 
            FROM events AS E 
            ORDER BY E.begin """
    )
    abstract fun getAllWithDetails(): Flow<List<FullEvent>>

    @Transaction
    @RawQuery(observedEntities = [CycleEvent::class])
    protected abstract fun getAllEvents(query : SimpleSQLiteQuery): Flow<List<FullEvent>>

    private fun buildSimpleQuery (
        whereClause : List<String>,
    ) : SimpleSQLiteQuery {
        val outputQuery = StringBuilder()
        outputQuery.append("SELECT E.* ")
        outputQuery.append("FROM events AS E ")
        if (whereClause.isNotEmpty()) {
            outputQuery.append("JOIN cycles AS C ON E.eid = C.cid ")
            outputQuery.append(whereClause.joinToString(" AND ", prefix = "WHERE ", postfix = " "))
        }
        outputQuery.append("ORDER BY E.begin ")
        val simpleSQLiteQuery = SimpleSQLiteQuery(outputQuery.toString(), arrayOf<String>())
        return simpleSQLiteQuery
    }

    fun query (filter : ScreenFilter) : Flow<List<FullEvent>> {
        val screen = when (filter) {
            ScreenFilter.ALL -> ""
            ScreenFilter.SYSTEM -> " ( C.screen = 'SYSTEM' OR C.screen = 'SYSTEM_LOCK' ) "
            ScreenFilter.LOCK -> " ( C.screen = 'LOCK' OR C.screen = 'SYSTEM_LOCK' ) "
        }
        val whereClause = mutableListOf<String>()
        if (screen.isNotEmpty()) whereClause.add(screen)
        return getAllEvents(buildSimpleQuery(whereClause))
    }

    @Transaction
    @Query("SELECT E.* FROM events AS E WHERE eid = :id ")
    abstract fun getById(id: Long?): Flow<FullEvent?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun create(event: CycleEvent): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(event: CycleEvent): Long

    @Delete
    abstract suspend fun delete(event: CycleEvent)

}
