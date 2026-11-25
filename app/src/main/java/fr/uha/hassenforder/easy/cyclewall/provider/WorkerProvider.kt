package fr.uha.hassenforder.easy.cyclewall.provider

import androidx.annotation.WorkerThread
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import fr.uha.hassenforder.easy.cyclewall.datastore.SettingsDataStore
import fr.uha.hassenforder.easy.cyclewall.worker.CycleWallWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class WorkerProvider (
    private val dispatcher: CoroutineDispatcher,
    private val settingsDataStore : SettingsDataStore,
    private val workManager : WorkManager,
) {

    private val jobId = settingsDataStore.getJobId()

    val jobsInfo : Flow<List<WorkInfo>> =
        workManager.getWorkInfosByTagFlow(CYCLEWALL_TIMER_TAG)

    private fun createCycleWallRequest(initialDelay : Long) : OneTimeWorkRequest {
        val constraints = Constraints.Builder().build()
        val request : OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<CycleWallWorker>()
                .setConstraints(constraints)
                .addTag(CYCLEWALL_TIMER_TAG)
                .setInitialDelay(initialDelay, TimeUnit.SECONDS)
                .build()
        return request
    }

    @WorkerThread
    suspend fun chainCycleWallJob (initialDelay : Long) = withContext(dispatcher) {
        workManager.enqueueUniqueWork(
            CYCLEWALL_TIMER_TAG,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            createCycleWallRequest(initialDelay)
        )
    }

    @WorkerThread
    suspend fun cancelCycleWallJob () = withContext(dispatcher) {
        workManager.cancelAllWorkByTag(CYCLEWALL_TIMER_TAG)
    }

    @WorkerThread
    suspend fun cancelAll () = withContext(dispatcher) {
        settingsDataStore.deleteJobId()
        workManager.cancelAllWork()
    }

    companion object {
        private const val CYCLEWALL_TIMER_TAG : String = "CycleWall.timer"
    }

}
