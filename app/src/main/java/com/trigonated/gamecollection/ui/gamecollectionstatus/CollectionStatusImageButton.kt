package com.trigonated.gamecollection.ui.gamecollectionstatus

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.core.content.withStyledAttributes
import com.trigonated.gamecollection.R

/**
 * Checkable ImageButton used to display collection statuses (wishlisted, owned).
 */
class CollectionStatusImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageButton(context, attrs, defStyleAttr), Checkable {
    /** Additional drawable state for the "checked" state. */
    private val STATES_CHECKED = intArrayOf(
        android.R.attr.state_checked
    )

    /** Whether this button is checked or not. */
    private var isChecked = false

    init {
        context.withStyledAttributes(attrs, R.styleable.CollectionStatusImageButton) {
            setChecked(getBoolean(R.styleable.CollectionStatusImageButton_checked, false))
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun setChecked(checked: Boolean) {
        if (isChecked == checked) {
            return
        }
        isChecked = checked
        refreshDrawableState()
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) mergeDrawableStates(drawableState, STATES_CHECKED)
        return drawableState
    }

    override fun onSaveInstanceState(): Parcelable {
        val state = Bundle()
        state.putParcelable("instanceState", super.onSaveInstanceState())
        state.putBoolean("state", isChecked)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            val outState = newState
            setChecked(outState.getBoolean("state"))
            newState = outState.getParcelable("instanceState")
        }
        super.onRestoreInstanceState(newState)
    }
}