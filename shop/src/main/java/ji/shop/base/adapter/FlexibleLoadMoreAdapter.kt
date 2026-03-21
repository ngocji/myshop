package ji.shop.base.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ji.shop.items.ProgressItemUi

class FlexibleLoadMoreAdapter<T : ItemUI<*>>(items: MutableList<T>) : FlexibleAdapter<T>(items) {
    private var attachedRecyclerView: RecyclerView? = null
    private var progressItem: ItemUI<*> = ProgressItemUi
    private var enableLoadMore = true
    private var isLoading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null

    private val endlessScrollListener by lazy {
        object : EndlessScrollListener(attachedRecyclerView?.layoutManager as LinearLayoutManager) {
            override fun loadMore() {
                if (enableLoadMore && !isLoading) {
                    isLoading = true
                    addItem(progressItem as T)
                    onLoadMoreListener?.onLoadMore()
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attachedRecyclerView = recyclerView
        setEnableLoadMore(enableLoadMore)
    }

    override fun updateDataset(newItems: List<T>, useDiff: Boolean) {
        super.updateDataset(newItems, useDiff)
        setEnableLoadMore(true)
    }

    fun onLoadMoreComplete(items: List<T>) {
        // remove progress item
        isLoading = false
        if (getItem(itemCount - 1) == progressItem) {
            removeItem(itemCount - 1)
        }
        addItems(items)
    }

    fun setLoadMoreListener(listener: OnLoadMoreListener): FlexibleLoadMoreAdapter<T> {
        onLoadMoreListener = listener
        return this
    }

    fun setProgressItem(item: ItemUI<*>) {
        progressItem = item
    }

    fun setEnableLoadMore(enable: Boolean) {
        this.enableLoadMore = enable
        if (attachedRecyclerView != null) {
            attachedRecyclerView?.removeOnScrollListener(endlessScrollListener)
            if (enable) {
                attachedRecyclerView?.addOnScrollListener(endlessScrollListener)
            }
        }
    }
}