package ji.shop.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FlexibleAdapter<T : ItemUI>(val items: MutableList<T>) : RecyclerView.Adapter<ItemUI>() {
    private val mTypeInstances = mutableMapOf<Int, T>()
    private val selectedItems = mutableSetOf<T>()
    private var mode = MULTI
    private val listeners = mutableSetOf<OnItemClickListener>()

    fun addListener(listener: OnItemClickListener): FlexibleAdapter<T> {
        listeners.add(listener)
        return this
    }

    fun setMode(mode: Int): FlexibleAdapter<T> {
        this.mode = mode
        return this
    }

    fun removeListener(listener: OnItemClickListener) {
        listeners.remove(listener)
    }

    override fun getItemViewType(position: Int): Int {
        val item = mapViewTypeFrom(items[position])
        return item.itemViewType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemUI {
        return mTypeInstances[viewType]?.createViewHolder(parent, viewType)?.apply {
            if (listeners.isNotEmpty()) {
                itemView.setOnClickListener { view ->
                    val position = absoluteAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listeners.forEach { it.onClick(this@FlexibleAdapter, view, position) }
                    }
                }
            }
        }
            ?: throw IllegalStateException()
    }

    override fun onBindViewHolder(holder: ItemUI, position: Int, payloads: List<Any?>) {
        mapViewTypeFrom(items[position]).bindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: ItemUI, position: Int) {
        mapViewTypeFrom(items[position]).bindViewHolder(holder, position, emptyList())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun mapViewTypeFrom(item: T): ItemUI {
        if (!mTypeInstances.containsKey(item.itemViewType)) {
            mTypeInstances[item.itemViewType] = item
        }
        return item
    }

    fun isSelected(position: Int): Boolean {
        return selectedItems.contains(items.getOrNull(position))
    }

    fun getSelectedItems() = selectedItems

    fun toggleSelection(position: Int): Boolean {
        val item = items.getOrNull(position) ?: return false
        if (isSelected(position)) {
            selectedItems.remove(item)
        } else {
            if (mode == SINGLE) {
                val previousSelectedPosition =
                    selectedItems.firstOrNull()?.let { items.indexOf(it) } ?: -1
                selectedItems.clear()
                if (previousSelectedPosition != -1) {
                    notifyItemChanged(previousSelectedPosition, Payload.SELECTION)
                }
            }

            selectedItems.add(item)
        }

        notifyItemChanged(position, Payload.SELECTION)
        return isSelected(position)
    }

    companion object {
        const val SINGLE = 0
        const val MULTI = 1
    }
}