package com.trigonated.gamecollection.ui.gamecollectionstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trigonated.gamecollection.databinding.FragmentGamecollectionstatusBinding
import com.trigonated.gamecollection.model.objects.GameCollectionStatus
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that displays an editable list of a game's collection statuses.
 *
 * Has navigation parameters: check [GameCollectionStatusViewModel] for details.
 */
@AndroidEntryPoint
class GameCollectionStatusFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentGamecollectionstatusBinding? = null

    /** This fragment's ViewModel. */
    private val viewModel: GameCollectionStatusViewModel by viewModels()

    /** This fragment's binding. */
    private val binding get() = _binding!!

    /** The list's adapter. */
    private val adapter: GameCollectionStatusAdapter get() = binding.list.adapter as GameCollectionStatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentGamecollectionstatusBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@GameCollectionStatusFragment.viewModel

            // Setup the list's adapter
            list.adapter = GameCollectionStatusAdapter().apply {
                itemStatusButtonClickListener =
                    object : GameCollectionStatusAdapter.ItemStatusButtonClickListener {
                        override fun onItemWishlistedStatusButtonClick(item: GameCollectionStatus) {
                            this@GameCollectionStatusFragment.viewModel.toggleItemWishlistedStatus(
                                item
                            )
                        }

                        override fun onItemOwnedStatusButtonClick(item: GameCollectionStatus) {
                            this@GameCollectionStatusFragment.viewModel.toggleItemOwnedStatus(item)
                        }
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
        adapter.itemStatusButtonClickListener = null
        _binding = null
    }
}