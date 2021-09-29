package com.trigonated.gamecollection.ui.home.wishlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trigonated.gamecollection.databinding.FragmentWishlistBinding
import com.trigonated.gamecollection.ui.generic.GameAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that displays the list of wishlisted games.
 */
@AndroidEntryPoint
class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null

    /** This fragment's ViewModel. */
    private val viewModel: WishlistViewModel by viewModels()

    /** This fragment's binding. */
    private val binding get() = _binding!!

    /** The list's adapter. */
    private val adapter: GameAdapter get() = binding.list.adapter as GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentWishlistBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@WishlistFragment.viewModel

            // Setup the list's adapter
            list.adapter = GameAdapter()
        }

        // Observe the ViewModel
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}