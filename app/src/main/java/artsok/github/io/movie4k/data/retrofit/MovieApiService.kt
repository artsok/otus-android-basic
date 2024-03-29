package artsok.github.io.movie4k.data.retrofit

import artsok.github.io.movie4k.BuildConfig
import artsok.github.io.movie4k.data.model.MovieDto
import artsok.github.io.movie4k.data.model.MovieListDto
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
    fun popularFilmsByPage(@Query("page") page: Int): Single<MovieListDto>

    @GET("movie/upcoming")
    fun upcomingFilmsByPage(@Query("page") page: Int): Single<MovieListDto>

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): Flowable<MovieListDto>

    @GET("movie/{id}")
    fun getMovie(@Path("id") id: Int): Single<MovieDto>

}

object MovieApi {
    val movieService: MovieApiService by lazy {
        buildService(
            MovieApiService::class.java
        )
    }
}