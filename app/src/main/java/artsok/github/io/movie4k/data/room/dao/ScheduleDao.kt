package artsok.github.io.movie4k.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import artsok.github.io.movie4k.data.model.Schedule
import io.reactivex.rxjava3.core.Single

@Dao
interface ScheduleDao {

    @Query("SELECT requestCode from schedule_table WHERE title LIKE :title AND time LIKE :time")
    fun getRequestCodeForScheduledMovie(title: String, time: String): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(info: Schedule)

    @Query("DELETE FROM schedule_table")
    fun deleteAll()

}