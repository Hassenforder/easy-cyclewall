package fr.uha.hassenforder.easy.cyclewall.worker

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.view.WindowManager
import androidx.core.graphics.scale
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import fr.uha.hassenforder.easy.cyclewall.model.CycleScreen
import fr.uha.hassenforder.easy.cyclewall.model.EventFactory
import fr.uha.hassenforder.easy.cyclewall.repository.WorkerRepository
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.lang.Thread.sleep
import java.time.LocalDateTime


@HiltWorker
class CycleWallWorker @AssistedInject constructor (
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val workerRepository: WorkerRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("CycleWallWorker", "Started")
        sleep (2000)
        val events = workerRepository.getFiltered().first()
        doCycleWall(events)
        val remainingTime = fetchNextTime(events)
        remainingTime?.let { doChainCall(it) }
        updateEvents (events)
        Log.d("CycleWallWorker", "Ended")
        return Result.success()
    }

    private fun installAsWallPaper (cycle : Cycle) {
        if (cycle.wallpaper == null) return
        val initialBitmap = loadBitmap(appContext, cycle.wallpaper) ?: return
        val resizedBitmap = resizeBitmap(appContext, initialBitmap)
        initialBitmap.recycle()
        val which = when (cycle.screen) {
            CycleScreen.SYSTEM -> WallpaperManager.FLAG_SYSTEM
            CycleScreen.LOCK -> WallpaperManager.FLAG_LOCK
            CycleScreen.SYSTEM_LOCK -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
        }
        WallpaperManager.getInstance(appContext).setBitmap(resizedBitmap, null, false, which)
        resizedBitmap.recycle()
    }

    private suspend fun doCycleWall(events : List<FullEvent>) {
        val factory = EventFactory()
        val now = LocalDateTime.now()
        val event = factory.bestEvent(events, now)
        when {
            event == null -> Log.d("CycleWallWorker", "No event found")
            event.cycle == null -> Log.d("CycleWallWorker", "Event has no cycle")
            event.cycle.wallpaper == null -> Log.d("CycleWallWorker", "Event has no wallpaper")
            else -> {
                Log.d("CycleWallWorker", "Event found: $event")
                installAsWallPaper(event.cycle)
            }
        }
    }

    private suspend fun fetchNextTime(events : List<FullEvent>) : Long? {
        val factory = EventFactory()
        val remainingTime = factory.nextTimeInSeconds(events, LocalDateTime.now())
        Log.d("CycleWallWorker", "Next time: $remainingTime")
        return remainingTime
    }

    private suspend fun updateEvents(events : List<FullEvent>) {
        val factory = EventFactory()
        val now = LocalDateTime.now()
        events
            .filter { factory.isAfter(now, it.event) }
            .filter { it.cycle != null }
            .filter { it.cycle?.rhythm != CycleRhythm.ONE_DAY }
            .forEach {
                val event = factory.buildEvent(it.cycle!!, now)
                if (event != null) {
                    workerRepository.update(event)
                    Log.d("CycleWallWorker", "Event updated: $event")
                }
            }

    }

    private suspend fun doChainCall(remainingTime : Long) {
        workerRepository.chainCycleWallJob(remainingTime)
    }

    companion object {
        private fun loadBitmap (context: Context, image : Uri) : Bitmap? {
            val source: ImageDecoder.Source = ImageDecoder.createSource(context.contentResolver, image)
            try {
                val bitmap = ImageDecoder.decodeBitmap(source)
                return bitmap
            } catch (e : IOException) {
                return null
            }
        }

        private fun resizeBitmap(context: Context, image : Bitmap) : Bitmap {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val bounds = windowManager.currentWindowMetrics.bounds
            val screenWidth = bounds.width()
            val screenHeight = bounds.height()

            val targetWidth = screenWidth
            val targetHeight = screenHeight

            val resizedBitmap = image.scale(targetWidth, targetHeight)
            return resizedBitmap
        }
    }

}