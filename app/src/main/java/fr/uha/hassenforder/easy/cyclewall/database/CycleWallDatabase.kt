package fr.uha.hassenforder.easy.cyclewall.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.hassenforder.easy.android.database.DatabaseTypeConverters
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.database.dao.EventDao
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleEvent

@TypeConverters(DatabaseTypeConverters::class)
@Database(
    entities = [
        Cycle::class,
        CycleEvent::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class CycleWallDatabase : RoomDatabase() {

    abstract fun getCycleDao () : CycleDao

    abstract fun getEventDao () : EventDao

    companion object {
        private lateinit var instance : CycleWallDatabase

        @Synchronized
        fun create (context : Context) : CycleWallDatabase {
            instance = Room.databaseBuilder(context, CycleWallDatabase::class.java, "cyclewall.db").build()
            return instance
        }

        @Synchronized
        fun get () : CycleWallDatabase {
            return instance
        }
    }

}
