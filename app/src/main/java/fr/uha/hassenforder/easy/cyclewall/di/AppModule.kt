package fr.uha.hassenforder.easy.cyclewall.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.hassenforder.easy.android.navigation.Navigator
import fr.uha.hassenforder.easy.android.navigation.NavigatorType
import fr.uha.hassenforder.easy.cyclewall.database.CycleWallDatabase
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.database.dao.EventDao
import fr.uha.hassenforder.easy.cyclewall.datastore.SettingsDataStore
import fr.uha.hassenforder.easy.cyclewall.navigation.GreetingDestination
import fr.uha.hassenforder.easy.cyclewall.navigation.rootNavGraph
import fr.uha.hassenforder.easy.cyclewall.provider.WorkerProvider
import fr.uha.hassenforder.easy.cyclewall.repository.CycleRepository
import fr.uha.hassenforder.easy.cyclewall.repository.EventRepository
import fr.uha.hassenforder.easy.cyclewall.repository.SettingsRepository
import fr.uha.hassenforder.easy.cyclewall.repository.WorkerRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = CycleWallDatabase.create(appContext)

    @Singleton
    @Provides
    fun provideCycleDao(db: CycleWallDatabase) = db.getCycleDao()

    @Singleton
    @Provides
    fun provideEventDao(db: CycleWallDatabase) = db.getEventDao()

    @Singleton
    @Provides
    fun provideNavigator() : Navigator = Navigator(
        startDestination = GreetingDestination.GreetingScreen,
        type = NavigatorType.SINGLETOP,
        navigationGraph = { rootNavGraph() }
    )

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile("cyclewall.pref") }
        )
    }

    @Singleton
    @Provides
    fun provideSettingsDataStore(store: DataStore<Preferences>) = SettingsDataStore(store)

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) : WorkManager {
        return WorkManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideWorkerProvider(
        ioDispatcher: CoroutineDispatcher,
        settingsDataStore : SettingsDataStore,
        workManager : WorkManager
    ) = WorkerProvider(ioDispatcher, settingsDataStore, workManager)

    @Singleton
    @Provides
    fun provideCycleRepository(
        ioDispatcher: CoroutineDispatcher,
        cycleDao: CycleDao,
        preferences: SettingsDataStore,
    ) = CycleRepository(ioDispatcher, cycleDao, preferences)

    @Singleton
    @Provides
    fun provideEventRepository(
        ioDispatcher: CoroutineDispatcher,
        eventDao: EventDao,
    ) = EventRepository(ioDispatcher, eventDao)

    @Singleton
    @Provides
    fun provideWorkerRepository(
        ioDispatcher: CoroutineDispatcher,
        eventDao: EventDao,
        workerProvider: WorkerProvider
    ) = WorkerRepository(ioDispatcher, eventDao, workerProvider)

    @Singleton
    @Provides
    fun provideSettingsRepository(
        ioDispatcher: CoroutineDispatcher,
        database: CycleWallDatabase,
        settingsDataStore : SettingsDataStore,
        workerProvider: WorkerProvider,
    ) = SettingsRepository(ioDispatcher, database, settingsDataStore, workerProvider)

}