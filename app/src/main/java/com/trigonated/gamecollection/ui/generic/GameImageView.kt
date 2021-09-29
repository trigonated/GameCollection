package com.trigonated.gamecollection.ui.generic

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import com.trigonated.gamecollection.R

/**
 * ImageView for a game image. Has appropriate placeholders for a game image.
 */
class GameImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    /** Image shown while the game image is loading. */
    @DrawableRes
    var placeholderResId: Int? = R.drawable.game_image_placeholder_loading

    /** Image shown when the game has no image or it failed loading. */
    @DrawableRes
    var errorResId: Int? = R.drawable.game_image_placeholder
}