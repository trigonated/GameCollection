package com.trigonated.gamecollection.ui.misc

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * Hide the soft keyboard.
 *
 * Note: This calls [androidx.fragment.app.Fragment.requireActivity].
 */
fun Fragment.hideSoftKeyboard() {
    val inputMethodService =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodService.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}