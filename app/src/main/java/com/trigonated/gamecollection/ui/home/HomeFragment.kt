package com.trigonated.gamecollection.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.databinding.FragmentHomeBinding

/**
 * Fragment that displays the game collection.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    /** This fragment's binding. */
    private val binding get() = _binding!!

    /** The ViewPager for the tabs. */
    private val viewPager get() = binding.viewPager

    /** The viewPager's adapter. */
    private val viewPagerAdapter: HomeViewPagerAdapter get() = viewPager.adapter as HomeViewPagerAdapter

    /** The BottomNavigation that contains the tabs. */
    private val bottomNavigation get() = binding.bottomNavigation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Setup the ViewPager and BottomNavigation
        viewPager.adapter = HomeViewPagerAdapter(this)
        viewPager.registerOnPageChangeCallback(viewPagerOnPageChangeCallback)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            // Change the ViewPager's page when a nav item is selected
            when (item.itemId) {
                R.id.wishlist, R.id.collection -> {
                    viewPager.setCurrentItem(
                        viewPagerAdapter.getItemIndexForBottomNavigationItemId(
                            item.itemId
                        ), true
                    )
                    true
                }
                else -> false
            }
        }

        // Setup the Toolbar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.unregisterOnPageChangeCallback(viewPagerOnPageChangeCallback)
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                navigateToSearch(requireView())
                return true
            }
            R.id.action_about -> {
                navigateToAbout(requireView())
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToSearch(view: View, query: String = "") {
        val direction =
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                query = query
            )
        view.findNavController().navigate(direction)
    }

    private fun navigateToAbout(view: View) {
        val direction =
            HomeFragmentDirections.actionHomeFragmentToAboutFragment()
        view.findNavController().navigate(direction)
    }

    /**
     * ViewPager callback that updates the BottomNavigation's selected item when the ViewPager's
     * page changes.
     */
    private val viewPagerOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            val selectedPageItemId =
                viewPagerAdapter.getBottomNavigationItemIdForItemIndex(position)
            if (bottomNavigation.selectedItemId != selectedPageItemId) {
                bottomNavigation.selectedItemId = selectedPageItemId
            }
        }
    }
}