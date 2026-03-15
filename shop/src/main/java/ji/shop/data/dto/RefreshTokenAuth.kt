package ji.shop.data.dto

import ji.shop.ShopSDK
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
            Api.create().refreshToken(refreshToken).data
        }

        if (newToken != null) {
            ShopSDK.init(
                authenticationToken = newToken.authenticationToken,
                accessToken = newToken.accessToken,
                refreshToken = newToken.refreshToken
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
        var onAuthFailedAction: (()-> Unit)? = null
    }
}