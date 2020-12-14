package artsok.github.io.movie4k.data.room.dao

import androidx.room.*
import artsok.github.io.movie4k.data.model.Movie
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDao {

    @Query("SELECT * from movie_table")
    fun getMovies(): Flowable<List<Movie>>

    @Query("SELECT * from movie_table WHERE favorite = 1")
    fun getFavoriteMovies(): Flowable<List<Movie>>

    @Query("SELECT * from movie_table WHERE scheduled = 1")
    fun getScheduleMovies(): Flowable<List<Movie>>

    @Query("SELECT COUNT(id) from movie_table WHERE favorite = 1")
    fun getFavoriteTotalRecordOfMovies(): Single<Int>

    @Query("SELECT COUNT(id) from movie_table")
    fun getTotalRecordOfMovies(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("DELETE FROM movie_table")
    fun deleteAll()

    @Query("UPDATE movie_table SET favorite = :favorite WHERE id LIKE :id")
    fun update(favorite: Int, id: Int)

    @Query("UPDATE movie_table SET favorite = :favorite WHERE title LIKE :title")
    fun update(favorite: Int, title: String)

    @Query("UPDATE movie_table SET scheduledTime = :scheduledTime WHERE id LIKE :id")
    fun updateScheduledTime(id: Int, scheduledTime: String)

    @Query("UPDATE movie_table SET scheduled = :scheduledFlag WHERE id LIKE :id")
    fun updateScheduledFlag(id: Int, scheduledFlag: Int)

    @Delete
    fun deleteFromDB(movie: Movie)

    fun updateScheduledFields(id: Int, scheduledTime: String) {
        updateScheduledFlag(id, 1)
        updateScheduledTime(id, scheduledTime)
    }
}