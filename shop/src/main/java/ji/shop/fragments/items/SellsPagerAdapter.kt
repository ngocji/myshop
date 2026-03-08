package ji.shop.fragments.items

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ji.shop.data.Group
import ji.shop.items.GroupItemUi

class SellsPagerAdapter(
    fragment: Fragment,
    val groups: List<GroupItemUi>
) : FragmentStateAdapter(
    fragment
) {
    override fun createFragment(index: Int): Fragment {
        return ProductsItemFragment.newInstance(
            groups[index].data.collectionId,
            groups[index].data.id
        )
    }

    override fun getItemCount(): Int {
        return groups.size
    }
}