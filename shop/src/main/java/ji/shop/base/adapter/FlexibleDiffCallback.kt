package ji.shop.base.adapter

import androidx.recyclerview.widget.DiffUtil

open class FlexibleDiffCallback<T : ItemUI<*>>(
    var oldList: List<T>,
    var newList: List<T>
) : DiffUtil.Callback() {

    open fun setItems(oldList: List<T>, newList: List<T>) {
        this.oldList = oldList
        this.newList = newList
    }

    open fun areItemTheSame(old: T?, new: T?): Boolean {
        return old?.getIdView() == new?.getIdView()
    }

    open fun areContentsTheSame(old: T?, new: T?): Boolean {
        return old == new
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemTheSame(
            oldList.getOrNull(oldItemPosition),
            newList.getOrNull(newItemPosition)
        )
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(
            oldList.getOrNull(oldItemPosition),
            newList.getOrNull(newItemPosition)
        )
    }
}