package fr.uha.hassenforder.easy.cyclewall.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.easy.cyclewall.database.CycleWallDatabase
import fr.uha.hassenforder.easy.cyclewall.database.FeedDatabase
import fr.uha.hassenforder.easy.cyclewall.datastore.SettingsDataStore
import fr.uha.hassenforder.easy.cyclewall.provider.WorkerProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SettingsRepository (
    private val dispatcher: CoroutineDispatcher,
    private val db : CycleWallDatabase,
    private val settingsDataStore : SettingsDataStore,
    private val workerProvider: WorkerProvider,
) {

    val jobsInfo = workerProvider.jobsInfo

    suspend fun populateDatabase (mode : Int, nextAlso : Boolean) = withContext(dispatcher) {
        FeedDatabase(db).populate(mode, nextAlso)
    }

    suspend fun clearDatabase () = withContext(dispatcher) {
        FeedDatabase(db).clear()
    }

    @WorkerThread
    suspend fun startJob () = withContext(dispatcher) {
        workerProvider.chainCycleWallJob(0L)
    }

    @WorkerThread
    suspend fun cancelJob () = withContext(dispatcher) {
        workerProvider.cancelCycleWallJob()
    }

    @WorkerThread
    suspend fun cancelAll () = withContext(dispatcher) {
        workerProvider.cancelAll()
    }

}
