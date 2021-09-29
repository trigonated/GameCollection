package com.trigonated.gamecollection.ui.home.collection

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.trigonated.gamecollection.databinding.FragmentCollectionBinding
import com.trigonated.gamecollection.ui.generic.GameAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that displays the list of owned games.
 */
@AndroidEntryPoint
class CollectionFragment : Fragment() {
    private var _binding: FragmentCollectionBinding? = null

    /** This fragment's ViewModel. */
    private val viewModel: CollectionViewModel by viewModels()

    /** This fragment's binding. */
    private val binding get() = _binding!!

    /** The list's adapter. */
    private val adapter: GameAdapter get() = binding.list.adapter as GameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentCollectionBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@CollectionFragment.viewModel

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