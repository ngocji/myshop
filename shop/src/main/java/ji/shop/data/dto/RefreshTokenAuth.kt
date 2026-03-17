package ji.shop.data.dto

import ji.shop.ShopSDK
import ji.shop.utils.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class RefreshTokenAuth : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            return null
        }

        val refreshToken = ShopSDK.getRefreshToken()

        val newToken = runBlocking {
            try {
                Api.create().refreshToken(
                    RequestGetRefreshToken(
                        refreshToken = refreshToken,
                        email = ShopSDK.getEmail(),
                        password = ShopSDK.getPassword()
                    )
                )
            } catch (e: Exception) {
                return@runBlocking null
            }
        }
        Log.d("Refresh token: $newToken")

        if (newToken != null) {
            ShopSDK.initToken(
                authenticationToken = newToken.authenticationToken.orEmpty(),
                accessToken = newToken.accessToken.orEmpty(),
                refreshToken = newToken.refreshToken.orEmpty()
            )
        } else {
            onAuthFailedAction?.invoke()
            return null
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${ShopSDK.getAccessToken()}")
            .build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var r = response.priorResponse
        while (r != null) {
            result++
            r = r.priorResponse
        }
        return result
    }

    companion object {
        var onAuthFailedAction: (() -> Unit)? = null
    }
}