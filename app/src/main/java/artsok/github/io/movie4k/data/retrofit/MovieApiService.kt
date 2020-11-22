package artsok.github.io.movie4k.data.retrofit

import artsok.github.io.movie4k.BuildConfig
import artsok.github.io.movie4k.data.model.MovieDto
import artsok.github.io.movie4k.data.model.MovieListDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

private val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        return@addInterceptor chain.proceed(
            chain
                .request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ZjM1MDI1ZjQ1MmFiMzI0ZmMyODMyY2ZiZDZmZTdlNSIsInN1YiI6IjVlZjBkZWI5YjBjZDIwMDAzNGI5MzdiZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.DEayxQXlrJT5t7cjGcmmuH2w2I8d32Auou5a-PidrjA"
                )
                .build()
        )
    }
    .addInterceptor(HttpLoggingInterceptor()
        .apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        })
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

fun <T> buildService(service: Class<T>): T {
    return retrofit.create(service)
}


/**
 * REST APIs
 * @URL https://developers.themoviedb.org/3/movies/get-movie-details
 */
interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularFilmsByPage(@Query("page") page: Int): MovieListDto

    @GET("movie/upcoming")
    suspend fun getUpcomingFilmsByPage(@Query("page") page: Int): MovieListDto

    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id: Int): MovieDto

}

object MovieApi {
    val movieService: MovieApiService by lazy {
        buildService(
            MovieApiService::class.java
        )
    }
}