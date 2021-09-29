package com.trigonated.gamecollection.ui.about

import androidx.lifecycle.ViewModel
import com.trigonated.gamecollection.misc.Constants
import com.trigonated.gamecollection.misc.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor() : ViewModel() {
    /** Fired when the viewmodel wants to open an url in the browser. */
    val onOpenUrlRequested = SingleLiveEvent<String>()

    /** Open the project's GitHub page. */
    fun openGitHubPage() = onOpenUrlRequested.call(Constants.GITHUB_PAGE_URL)

    /** Open the RAWG website. */
    fun openRawgPage() = onOpenUrlRequested.call(Constants.RAWG_URL)
}