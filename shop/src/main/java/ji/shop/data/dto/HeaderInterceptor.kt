package ji.shop.data.dto

import ji.shop.ShopSDK
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer ${ShopSDK.getAccessToken()}")
            .build()
        return chain.proceed(newRequest)
    }
}