package com.trigonated.gamecollection.ui.misc

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Utility functions for dealing with intents.
 */
object IntentUtils {
    /**
     * Launch a share intent, which will show a "Share" dialog to share the provided [url].
     * @param context The context.
     * @param subject A subject/title that might be used by some applications.
     * @param url The url being shared.
     */
    fun shareUrl(context: Context, subject: String, url: String): Boolean {
        return try {
            context.startActivity(Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }, null))
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Launch a view intent, which usually opens the web-browser on the provided [url].
     * @param context The context.
     * @param url The url to open.
     */
    fun openUrl(context: Context, url: Uri): Boolean {
        return try {
            context.startActivity(Intent(Intent.ACTION_VIEW, url))
            true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Launch a view intent, which usually opens the web-browser on the provided [url].
     * @param context The context.
     * @param url The url to open.
     */
    fun openUrl(context: Context, url: String): Boolean {
        return try {
            openUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}