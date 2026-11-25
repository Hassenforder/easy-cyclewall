package fr.uha.hassenforder.easy.cyclewall.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.easy.cyclewall.database.dao.EventDao
import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import fr.uha.hassenforder.easy.cyclewall.model.CycleEvent
import fr.uha.hassenforder.easy.cyclewall.provider.WorkerProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WorkerRepository (
    private val dispatcher: CoroutineDispatcher,
    private val eventDao : EventDao,
    private val workerProvider: WorkerProvider,
) {

    fun getFiltered() : Flow<List<FullEvent>> {
        return eventDao.getAllWithDetails()
    }

    @WorkerThread
    suspend fun update(event: CycleEvent) = withContext(dispatcher) {
        eventDao.update(event)
    }

    suspend fun chainCycleWallJob(remainingTime: Long) {
        workerProvider.chainCycleWallJob(remainingTime)
    }

}