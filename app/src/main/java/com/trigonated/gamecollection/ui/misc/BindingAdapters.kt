package com.trigonated.gamecollection.ui.misc

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.color.MaterialColors
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.misc.Result
import com.trigonated.gamecollection.misc.withAlpha
import com.trigonated.gamecollection.ui.generic.DataStatusView
import com.trigonated.gamecollection.ui.generic.GameImageView
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation

// ImageView

/**
 * Binds an url of an image to a [ImageView], which will load the image from the provided url.
 * @param view The View to bind to.
 * @param imageUrl The url of the image.
 */
@BindingAdapter("srcUrl")
fun bindImageViewSrcUrl(view: ImageView, imageUrl: String?) {
    var request = Glide.with(view.context)
        .load(imageUrl)
        .transition(DrawableTransitionOptions.withCrossFade())
    if (view is GameImageView) {
        view.placeholderResId?.let { request = request.placeholder(it) }
        view.errorResId?.let { request = request.error(it) }
    }
    request.into(view)
}

/**
 * Binds an url of an image to a [ImageView], which will load the image from the provided url with
 * some transformations appropriate for an image being used on the AppBar.
 * @param view The View to bind to.
 * @param imageUrl The url of the image.
 */
@BindingAdapter("srcUrlAppBarStyle")
fun bindImageViewSrcUrlAppBarStyle(view: ImageView, imageUrl: String?) {
    val tint: Int = MaterialColors.getColor(view.context, R.attr.colorPrimary, Color.GRAY)
        .withAlpha(127)

    Glide.with(view.context)
        .load(imageUrl)
        .apply(
            RequestOptions.bitmapTransform(
                MultiTransformation(
                    BlurTransformation(25, 3),
                    ColorFilterTransformation(tint)
                )
            )
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

// TextView

/**
 * Binds a [String] to a [TextView], displaying the string as html text.
 * @param view The View to bind to.
 * @param htmlText The html that will be bound.
 */
@BindingAdapter("htmlText")
fun bindTextViewHtmlText(view: TextView, htmlText: String?) {
    if (htmlText != null) {
        @Suppress("DEPRECATION")
        view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
        else
            Html.fromHtml(htmlText)
    } else {
        view.text = null
    }
}

// View

@BindingAdapter("isVisible")
fun bindViewIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

// DataStatusView

@BindingAdapter("errorText")
fun bindDataStatusViewErrorText(view: DataStatusView, errorText: String?) {
    view.errorText = errorText
}

@BindingAdapter("noDataText")
fun bindDataStatusViewNoDataText(view: DataStatusView, noDataText: String?) {
    view.noDataText = noDataText
}

@BindingAdapter("status")
fun bindDataStatusViewStatus(view: DataStatusView, status: Result.Status?) {
    view.status = status
}

@BindingAdapter("hasData")
fun bindDataStatusViewHasData(view: DataStatusView, hasData: Boolean?) {
    view.hasData = hasData ?: false
}