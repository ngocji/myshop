package ji.shop.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ItemUI(val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun createViewHolder(parent: ViewGroup, viewType: Int): ItemUI

    abstract fun bindViewHolder(holder: ItemUI, position: Int, payloads: List<Any?>)

    fun getItemViewType(position: Int) = 0

    fun <VB : ViewBinding> bind(action: VB.() -> Unit) {
        action(binding as? VB ?: return)
    }

}