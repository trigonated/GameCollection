package com.trigonated.gamecollection.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.trigonated.gamecollection.BuildConfig
import com.trigonated.gamecollection.databinding.FragmentAboutBinding
import com.trigonated.gamecollection.ui.misc.IntentUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that displays the app's "about" information.
 */
@AndroidEntryPoint
class AboutFragment : Fragment() {
    /** This fragment's ViewModel. */
    private val viewModel: AboutViewModel by viewModels()
    private var _binding: FragmentAboutBinding? = null

    /** This fragment's binding. */
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the UI binding
        _binding = FragmentAboutBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AboutFragment.viewModel
            version = BuildConfig.VERSION_NAME

            // Setup the Toolbar
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
        }

        // Observe the ViewModel
        viewModel.onOpenUrlRequested.observe(viewLifecycleOwner) { url ->
            IntentUtils.openUrl(requireContext(), url)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}