package artsok.github.io.movie4k.data.model

import com.google.gson.annotations.SerializedName

data class MovieListDataModel(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResult: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<MovieDataModel>
)