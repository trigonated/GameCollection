package com.trigonated.gamecollection.misc

import android.graphics.Color
import androidx.annotation.IntRange

/**
 * Returns a copy of this [Int] with the specified [alpha].
 * @param alpha The new alpha channel value.
 */
fun Int.withAlpha(@IntRange(from = 0, to = 255) alpha: Int): Int {
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}