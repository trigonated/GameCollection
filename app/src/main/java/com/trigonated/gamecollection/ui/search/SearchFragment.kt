package com.trigonated.gamecollection.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.databinding.FragmentSearchBinding
import com.trigonated.gamecollection.ui.misc.hideSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that searches for games.
 *
 * Has navigation parameters: check [SearchViewModel] for details.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null

    /** This fragment's ViewModel. */
    private val viewModel: SearchViewModel by viewModels()

    /** This fragment's binding. */
    private val binding get() = _binding!!

    /** The list's adapter. */
    private val adapter: SearchResultAdapter get() = binding.list.adapter as SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchFragment.viewModel

            // Setup the list adapter
            list.adapter = SearchResultAdapter()

            // Setup the search EditText
            searchEdittext.setOnEditorActionListener { v, actionId, event ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        search()
                        true
                    }
                    else -> false
                }
            }

            // Setup the Toolbar
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_search -> {
                        search()
                        true
                    }
                    else -> false
                }
            }
        }

        // Observe the ViewModel
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun search() {
        hideSoftKeyboard()
        this@SearchFragment.viewModel.search()
    }
}