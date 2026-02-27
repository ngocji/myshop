package ji.shop.base.adapter

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class ItemUI<VB : ViewBinding>() {
    fun getIdView(): String = "${hashCode()}"

    abstract fun createViewHolder(adapter: FlexibleAdapter<*>, parent: ViewGroup, viewType: Int): ItemViewHolder

    abstract fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: ItemViewHolder, position: Int, payloads: List<Any?>)

    open fun getItemViewType() = 0

    fun withBinding(holder: ItemViewHolder, action: VB.() -> Unit) {
        action(holder.binding as VB)
    }
}