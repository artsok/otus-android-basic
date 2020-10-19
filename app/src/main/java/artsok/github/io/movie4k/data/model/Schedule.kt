package artsok.github.io.movie4k.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
class Schedule(
    val title: String,
    val requestCode: Int,
    val time: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}