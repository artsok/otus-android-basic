package artsok.github.io.movie4k.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import artsok.github.io.movie4k.data.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * from movie_table")
    fun getMovies(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("DELETE FROM movie_table")
    fun deleteAll()

    @Query("SELECT COUNT(id) from movie_table")
    fun getTotalRecordOfMovies(): Int

    @Query("UPDATE movie_table SET favorite = :favorite WHERE id LIKE :id")
    fun update(favorite: Int, id: Int)

    @Query("UPDATE movie_table SET favorite = :favorite WHERE title LIKE :title")
    fun update(favorite: Int, title: String)

    @Query("SELECT * from movie_table WHERE favorite = 1")
    fun getFavoriteMovies(): LiveData<List<Movie>>

    @Query("SELECT COUNT(id) from movie_table WHERE favorite = 1")
    fun getFavoriteTotalRecordOfMovies(): Int

    @Delete
    fun deleteFromDB(movie: Movie)

}