package ji.shop.items

import ji.shop.base.adapter.OnItemClickListener

interface CountChangOnItemListener : OnItemClickListener {
    fun onCountChanged(position: Int, count: Int)
}