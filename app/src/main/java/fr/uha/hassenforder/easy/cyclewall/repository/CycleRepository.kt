package fr.uha.hassenforder.easy.cyclewall.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.android.database.model.ManagementState
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.ScreenFilter
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.CycleUpdate
import fr.uha.hassenforder.easy.cyclewall.datastore.SettingsDataStore
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CycleRepository(
    private val dispatcher: CoroutineDispatcher,
    private val cycleDao: CycleDao,
    private val preference : SettingsDataStore,
) {

    val isDayWithTime = preference.isDayWithTime()

    fun getAll(): Flow<List<Cycle>> {
        return cycleDao.getAll()
    }

    fun getAllWithDetails(): Flow<List<Cycle>> {
        return cycleDao.getAllWithDetails()
    }

    fun getFilteredWithDetails(): Flow<List<Cycle>> {
        return cycleDao.getFilteredWithDetails()
    }

    fun query(mode : ListMode, filter : ScreenFilter): Flow<List<Cycle>> {
        return cycleDao.query(mode, filter)
    }

    fun getById(id: Long): Flow<Cycle?> {
        return cycleDao.getById(id)
    }

    @WorkerThread
    suspend fun update(content : CycleUpdate) = withContext(dispatcher) {
        when (content) {
            is CycleUpdate.Title -> cycleDao.update(content)
            is CycleUpdate.Rhythm -> cycleDao.update(content)
            is CycleUpdate.Screen -> cycleDao.update(content)
            is CycleUpdate.Begin -> cycleDao.update(content)
            is CycleUpdate.Ending -> cycleDao.update(content)
            is CycleUpdate.Wallpaper -> cycleDao.update(content)
            is CycleUpdate.Management -> cycleDao.update(content)
        }
    }

    @WorkerThread
    suspend fun create(cycle: Cycle): Long {
        return cycleDao.create(cycle)
    }

    @WorkerThread
    suspend fun valid(cid: Long, valid: Boolean) {
        if (valid) {
            cycleDao.update(CycleUpdate.Management(cid, ManagementState.VALID ))
        } else {
            cycleDao.update(CycleUpdate.Management(cid, ManagementState.INVALID))
        }
    }

    @WorkerThread
    suspend fun delete(cycle: Cycle) {
        cycleDao.delete(cycle)
    }

    @WorkerThread
    suspend fun edit(cid: Long): Long = cid

    @WorkerThread
    suspend fun hide(cid: Long): Boolean {
        cycleDao.update(CycleUpdate.Management(cid, ManagementState.HIDDEN))
        return true
    }

    @WorkerThread
    suspend fun show(cid: Long): Boolean {
        cycleDao.update(CycleUpdate.Management(cid, ManagementState.VALID))
        return true
    }

}
