package ji.shop.exts

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun buildOkHttpClient(enableLog: Boolean = true,
                      authenticator: Authenticator? = null,
                      vararg interceptors: Interceptor): OkHttpClient {
    val timeout = 15L
    return OkHttpClient.Builder()
        .apply {
            interceptors.forEach {
                addInterceptor(it)
            }

            if (enableLog) {
                addInterceptor(HttpLoggingInterceptor())
            }
            if (authenticator != null) {
                authenticator(authenticator)
            }
        }
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .callTimeout(timeout, TimeUnit.SECONDS)
        .build()
}


inline fun <reified T> buildApiService(
    baseUrl: String,
    httpClient: OkHttpClient,
    converterFactor: Converter.Factory = GsonConverterFactory.create()
): T {
    return Retrofit.Builder()
        .client(httpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactor)
        .build()
        .create(T::class.java)
}
