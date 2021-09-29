package com.trigonated.gamecollection.ui.gamedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.databinding.FragmentGamedetailsBinding
import com.trigonated.gamecollection.model.objects.Platform
import com.trigonated.gamecollection.ui.misc.IntentUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that displays the details of a game.
 *
 * Has navigation parameters: check [GameDetailsViewModel] for details.
 */
@AndroidEntryPoint
class GameDetailsFragment : Fragment() {
    private var _binding: FragmentGamedetailsBinding? = null

    /** This fragment's ViewModel. */
    private val viewModel: GameDetailsViewModel by viewModels()

    /** This fragment's binding. */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentGamedetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@GameDetailsFragment.viewModel

            // Setup the toolbar
            var isToolbarShown = false
            gameDetailScrollview.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                    // Check if the toolbar must be visible.
                    val shouldShowToolbar = scrollY > toolbar.height
                    // Update the toolbar if it changed
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar
                        appBarLayout.isActivated = shouldShowToolbar
                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                    }
                }
            )
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        this@GameDetailsFragment.viewModel.shareGame()
                        true
                    }
                    R.id.action_open_in_browser -> {
                        this@GameDetailsFragment.viewModel.openGameInBrowser()
                        true
                    }
                    else -> false
                }
            }
        }

        // Observe the ViewModel
        viewModel.game.observe(viewLifecycleOwner) { game ->
            binding.gamePlatformsChips.removeAllViews()
            for (platform: Platform in game.platforms) {
                binding.gamePlatformsChips.addView(Chip(requireContext()).apply {
                    text = platform.name
                })
            }
        }
        viewModel.onNavigateToGameCollectionStatusRequested.observe(viewLifecycleOwner) {
            navigateToGameCollectionStatus(viewModel.gameId)
        }
        viewModel.onShareUrlRequested.observe(viewLifecycleOwner) { info ->
            IntentUtils.shareUrl(requireContext(), info.title, info.url)
        }
        viewModel.onOpenUrlRequested.observe(viewLifecycleOwner) { url ->
            IntentUtils.openUrl(requireContext(), url)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToGameCollectionStatus(gameId: String) {
        val direction =
            GameDetailsFragmentDirections.actionGameDetailsFragmentToGameCollectionStatusFragment(
                gameId = gameId
            )
        requireView().findNavController().navigate(direction)
    }
}