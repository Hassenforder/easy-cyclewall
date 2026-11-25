package fr.uha.hassenforder.easy.cyclewall

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Inject

@HiltAndroidApp
class CycleWallApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory) // Hilt provides this automatically
            .setExecutor(Dispatchers.Default.asExecutor())
            .setTaskExecutor(Dispatchers.Default.asExecutor())
            .build()

}