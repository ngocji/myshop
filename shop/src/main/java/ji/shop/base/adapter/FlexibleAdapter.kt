package ji.shop.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class FlexibleAdapter<T : ItemUI<*>>(var items: MutableList<T>) :
    RecyclerView.Adapter<ItemViewHolder>() {
    private val mTypeInstances = mutableMapOf<Int, T>()
    private val selectedItems = mutableSetOf<T>()
    private var mode = MULTI
    private val listeners = mutableSetOf<OnItemClickListener>()
    private var diffUtilCallback: FlexibleDiffCallback<T>? = null


    fun addAdjustSelected(position: Int) {
        items.getOrNull(position)?.let {
            selectedItems.add(it)
        }
    }

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
        return item.getItemViewType()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return mTypeInstances[viewType]?.createViewHolder(this, parent, viewType)?.apply {
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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: List<Any?>) {
        mapViewTypeFrom(items[position]).bindViewHolder(this, holder, position, payloads)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        mapViewTypeFrom(items[position]).bindViewHolder(this, holder, position, emptyList())
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun mapViewTypeFrom(item: T): ItemUI<*> {
        if (!mTypeInstances.containsKey(item.getItemViewType())) {
            mTypeInstances[item.getItemViewType()] = item
        }
        return item
    }

    fun isSelected(position: Int): Boolean {
        return selectedItems.contains(items.getOrNull(position))
    }

    fun getSelectedItems() = selectedItems

    fun getSelectedPositions() = selectedItems.mapNotNull { item ->
        items.indexOf(item).takeIf { it != -1 }
    }

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

    fun updateDataset(newItems: List<T>, useDiff: Boolean = true) {
        if (useDiff) {
            if (diffUtilCallback == null) {
                diffUtilCallback = FlexibleDiffCallback(items, newItems)
            }
            diffUtilCallback?.also {
                it.setItems(items, newItems)
                val diffResult = DiffUtil.calculateDiff(it, false)
                items = it.newList.toMutableList()
                diffResult.dispatchUpdatesTo(this)
            }
            return
        }

        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    fun clearAdjustSelection() {
        selectedItems.clear()
    }

    fun clearSelection() {
        val selectedPositions = selectedItems.mapNotNull { item -> items.indexOf(item).takeIf {  it != -1 } }
            .sortedDescending()
        selectedItems.clear()
        selectedPositions.forEach { notifyItemChanged(it, Payload.SELECTION) }
    }

    fun getItem(position: Int): T? {
        return items.getOrNull(position)
    }

    fun notifyListeners(action: OnItemClickListener.()-> Unit) {
        listeners.forEach(action)
    }

    fun find(action: T.() -> Boolean):T? {
        return items.find(action)
    }

    companion object {
        const val SINGLE = 0
        const val MULTI = 1
    }
}