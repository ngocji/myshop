package ji.shop.data

import ji.shop.exts.buildApiService
import ji.shop.exts.buildOkHttpClient

interface Api {

    companion object {
        fun create(token: String): Api {
            return buildApiService(
                "https://xxx.xxx/",
                buildOkHttpClient(enableLog = true, HeaderInterceptor(token))
            )
        }
    }
}
