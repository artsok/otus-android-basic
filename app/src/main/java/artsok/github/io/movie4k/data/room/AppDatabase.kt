package artsok.github.io.movie4k.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import artsok.github.io.movie4k.data.model.Movie
import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.data.room.dao.MovieDao
import artsok.github.io.movie4k.data.room.dao.ScheduleDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Movie::class, Schedule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val dbName = "database"
            context.applicationContext.deleteDatabase(dbName)
            return Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, dbName
            )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
        }

        private class DatabaseCallback(private val scope: CoroutineScope) :
            RoomDatabase.Callback() {
            /**
             * Clear the database every time it is created or opened (for debug mode)
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        database.movieDao().deleteAll()
                        database.scheduleDao().deleteAll()
                    }
                }
            }
        }
    }
}
