package ji.shop.exts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ViewGroup.layoutInflate() = LayoutInflater.from(context)

fun ImageView.load(path: Any?, error: Int = 0) {
    Glide.with(this)
        .load(path)
        .error(error)
        .into(this)
}

fun View.changeEnabled(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}