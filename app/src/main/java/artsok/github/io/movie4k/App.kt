//package artsok.github.io.movie4k
//
//import android.app.Application
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import artsok.github.io.movie4k.data.retrofit.MovieApiService
//import okhttp3.Cache
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//private const val BASE_URL = "https://api.themoviedb.org/3/"
//private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
//
//class App : Application() {
//
//   // var movieService: MovieApiService by Lazy {}
//
//    val movieService: MovieApiService by lazy {
//        buildService(
//            MovieApiService::class.java
//        )
//    }
//    lateinit var retrofit: Retrofit
//
//    companion object {
//        lateinit var instance: App
//            private set
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        instance = this
//        initRetrofit()
//    }
//
//
//
//    private fun initRetrofit() {
//        retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(initClient())
//            .build()
//    }
//
//    private fun <T> buildService(service: Class<T>): T {
//        return retrofit.create(service)
//    }
//
//    private fun initClient(): OkHttpClient {
//        val myCache = Cache(applicationContext.cacheDir, CACHE_SIZE)
//        return OkHttpClient.Builder()
//            .cache(myCache)
//            .addInterceptor { chain ->
//                return@addInterceptor chain.proceed(
//                    chain
//                        .request()
//                        .newBuilder()
//                        .addHeader(
//                            "Authorization",
//                            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ZjM1MDI1ZjQ1MmFiMzI0ZmMyODMyY2ZiZDZmZTdlNSIsInN1YiI6IjVlZjBkZWI5YjBjZDIwMDAzNGI5MzdiZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.DEayxQXlrJT5t7cjGcmmuH2w2I8d32Auou5a-PidrjA"
//                        )
//                        .build()
//                )
//            }
//            .addInterceptor(HttpLoggingInterceptor()
//                .apply {
//                    if (BuildConfig.DEBUG) {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    }
//                })
//            .addInterceptor { chain ->
//                var request = chain.request()
//                request = if (hasNetwork(applicationContext)!!)
//                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
//                else
//                    request.newBuilder().header(
//                        "Cache-Control",
//                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
//                    ).build()
//                chain.proceed(request)
//            }
//            .build()
//    }
//
//    private fun hasNetwork(context: Context): Boolean? {
//        var isConnected: Boolean? = false
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        if (activeNetwork != null && activeNetwork.isConnected)
//            isConnected = true
//        return isConnected
//    }
//}
