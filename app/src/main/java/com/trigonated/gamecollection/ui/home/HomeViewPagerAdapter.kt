package com.trigonated.gamecollection.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.ui.home.collection.CollectionFragment
import com.trigonated.gamecollection.ui.home.wishlist.WishlistFragment

private const val WISHLIST_TAB_INDEX = 0
private const val COLLECTION_TAB_INDEX = 1

/**
 * ViewPager Adapter for the Home Fragment.
 */
class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            WISHLIST_TAB_INDEX -> WishlistFragment()
            COLLECTION_TAB_INDEX -> CollectionFragment()
            else -> throw Error("Invalid position: $position")
        }
    }

    fun getBottomNavigationItemIdForItemIndex(itemIndex: Int): Int {
        return when (itemIndex) {
            WISHLIST_TAB_INDEX -> R.id.wishlist
            COLLECTION_TAB_INDEX -> R.id.collection
            else -> throw Error("Invalid itemIndex: $itemIndex")
        }
    }

    fun getItemIndexForBottomNavigationItemId(itemId: Int): Int {
        return when (itemId) {
            R.id.wishlist -> WISHLIST_TAB_INDEX
            R.id.collection -> COLLECTION_TAB_INDEX
            else -> throw Error("Invalid itemId: $itemId")
        }
    }
}