package fr.uha.hassenforder.easy.cyclewall.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import javax.inject.Inject

data class Settings(
    val withTime: Boolean,
    val jobId: UUID?,
)

class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Optimized flow that maps all preferences once.
     * distinctUntilChanged() ensures we only emit when actual data changes.
     */
    val settings: Flow<Settings> = dataStore.data
        .catch {
            emit(emptyPreferences())
        }
        .map { preferences ->
            Settings(
                withTime = preferences[KEY_WITH_TIME] ?: false,
                jobId = preferences[KEY_JOB_ID]?.let {
                    try { UUID.fromString(it) } catch (e: Exception) { null }
                },
            )
        }
        .distinctUntilChanged()

    fun isDayWithTime(): Flow<Boolean> = settings.map { it.withTime }.distinctUntilChanged()

    fun getJobId(): Flow<UUID?> = settings.map { it.jobId }.distinctUntilChanged()

    suspend fun saveDayWithTime(withTime: Boolean) {
        dataStore.edit { preference ->
            preference[KEY_WITH_TIME] = withTime
        }
    }

    /**
     * Returns the created UUID. Creation is now handled explicitly,
     * which is safer than hidden side effects in a Flow getter.
     */
    suspend fun createJobId(): UUID {
        val id = UUID.randomUUID()
        dataStore.edit { preferences ->
            preferences[KEY_JOB_ID] = id.toString()
        }
        return id
    }

    suspend fun deleteJobId() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_JOB_ID)
        }
    }

    companion object {
        private val KEY_WITH_TIME = booleanPreferencesKey("withTime")
        private val KEY_JOB_ID = stringPreferencesKey("jobId")
    }

}