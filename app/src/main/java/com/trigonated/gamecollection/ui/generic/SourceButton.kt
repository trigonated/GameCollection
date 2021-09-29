package com.trigonated.gamecollection.ui.generic

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.misc.Constants
import com.trigonated.gamecollection.ui.misc.IntentUtils

/**
 * Button that displays "Source: <something>" and opens the source's webpage when clicked.
 */
class SourceButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.sourceButtonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {
    init {
        setOnClickListener { IntentUtils.openUrl(context, Constants.RAWG_URL) }
    }
}