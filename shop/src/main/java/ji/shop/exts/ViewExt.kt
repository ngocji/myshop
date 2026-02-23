package ji.shop.exts

import android.view.LayoutInflater
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