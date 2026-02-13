package ji.shop.exts

import android.content.Context
import android.content.res.Resources
import ji.shop.R

fun Context?.isTablet() = this?.resources?.getBoolean(R.bool.isTablet)
    ?: Resources.getSystem().displayMetrics.let { displayMetrics ->
        val widthDp = displayMetrics.widthPixels / displayMetrics.density
        val heightDp = displayMetrics.heightPixels / displayMetrics.density
        val smallestWidthDp = minOf(widthDp, heightDp)
        return smallestWidthDp >= 600
    }