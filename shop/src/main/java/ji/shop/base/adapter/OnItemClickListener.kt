package ji.shop.base.adapter

import android.view.View

fun interface OnItemClickListener {
    fun onClick(adapter: FlexibleAdapter<*>, view: View, position: Int)
}