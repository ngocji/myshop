package ji.shop.fragments

import android.os.Bundle
import android.view.View
import ji.shop.R
import ji.shop.base.BaseFragment

class InventoryFragment: BaseFragment(R.layout.fragment_inventory) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    private fun initViews() {

    }

    private fun initObserves() {

    }
}