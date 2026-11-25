package fr.uha.hassenforder.easy.cyclewall.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.ScreenFilter
import fr.uha.hassenforder.easy.cyclewall.database.dao.EventDao
import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleEvent
import fr.uha.hassenforder.easy.cyclewall.model.EventFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class EventRepository (
    private val dispatcher: CoroutineDispatcher,
    private val eventDao : EventDao,
) {

    fun query(filter : ScreenFilter): Flow<List<FullEvent>> {
        return eventDao.query(filter)
    }

    @WorkerThread
    suspend fun create(next: CycleEvent): Long = eventDao.create(next)

    @WorkerThread
    suspend fun refreshAll(events: List<FullEvent>, now : LocalDateTime) {
        val factory = EventFactory()
        events.filter { it.cycle != null }.forEach { event ->
            if (event.cycle == null) return@forEach
            val event = factory.buildEvent(event.cycle, now)
            if (event != null) {
                eventDao.update(event)
            }
        }
    }

    @WorkerThread
    suspend fun refresh(event: FullEvent, now : LocalDateTime) {
        if (event.cycle == null) return
        val factory = EventFactory()
        val event = factory.buildEvent(event.cycle, now)
        if (event != null) {
            eventDao.update(event)
        }
    }

    @WorkerThread
    suspend fun update(event: CycleEvent) {
        eventDao.update(event)
    }

    @WorkerThread
    suspend fun delete(event: FullEvent) {
        eventDao.delete(event.event)
    }

    @WorkerThread
    suspend fun view(nid: Long): Long = nid

}