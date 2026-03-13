package ji.shop.data.dto

import ji.shop.exts.buildApiService
import ji.shop.exts.buildOkHttpClient

interface Api {

    companion object {
        fun create(): Api {
            return buildApiService(
                "https://api-staging.showslinger.com/",
                buildOkHttpClient(enableLog = true, HeaderInterceptor())
            )
        }
    }
}
