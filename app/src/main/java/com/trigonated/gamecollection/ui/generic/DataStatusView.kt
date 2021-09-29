package com.trigonated.gamecollection.ui.generic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.trigonated.gamecollection.R
import com.trigonated.gamecollection.misc.Result

/**
 * View with multiple states (loaded, loading, error, etc) visible depending on the status of some
 * data.
 *
 * On init, it automatically takes it's child as it's "loaded" view.
 *
 * See [R.styleable.DataStatusView] for properties used to customize this view.
 */
class DataStatusView : FrameLayout {
    /** Whether this view has completed it's initialization or not. */
    private var isInitialized: Boolean = false

    /** Error message shown when there's an error. */
    var errorText: String? = null
        set(value) {
            field = value
            updateState()
        }

    /**
     * Error message shown when there's no data (no items). See [hasData].
     */
    var noDataText: String? = null
        set(value) {
            field = value
            updateState()
        }

    /** The status that controls which states are visible. */
    var status: Result.Status? = null
        set(value) {
            field = value
            updateState()
        }

    /** Whether to show a "no data/no items" view. */
    var hasData: Boolean = true
        set(value) {
            field = value
            updateState()
        }

    /** View shown when [status] is successful and [hasData] is true. */
    private lateinit var loadedView: View

    /** View shown when [status] is successful and [hasData] is false. */
    private lateinit var loadedNoDataView: View

    /** View shown when [status] is loading. */
    private lateinit var loadingView: View

    /** View shown when [status] is error. */
    private lateinit var errorView: View

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(attrs, R.styleable.DataStatusView, defStyle, 0)
        errorText = a.getString(R.styleable.DataStatusView_errorText)
            ?: resources.getString(R.string.generic_error_occurred)
        noDataText = a.getString(R.styleable.DataStatusView_noDataText)
            ?: resources.getString(R.string.generic_no_data)
        val loadingLayoutResId: Int = a.getResourceId(
            R.styleable.DataStatusView_loadingLayout,
            R.layout.view_datastatusview_loading
        )
        val errorLayoutResId: Int = a.getResourceId(
            R.styleable.DataStatusView_errorLayout,
            R.layout.view_datastatusview_error
        )
        val noDataLayoutResId: Int = a.getResourceId(
            R.styleable.DataStatusView_noDataLayout,
            R.layout.view_datastatusview_nodata
        )
        a.recycle()

        // Setup the "loaded" view
        if (childCount > 0) {
            // Set the child view as the "loaded" view
            loadedView = getChildAt(0)
        } else {
            // Create an empty "loaded" view
            loadedView = View(context)
            addView(loadedView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        // Setup the "loaded with no data" view
        loadedNoDataView = inflate(context, noDataLayoutResId, null)
        addView(loadedNoDataView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        // Setup the "loading" view
        loadingView = inflate(context, loadingLayoutResId, null)
        addView(loadingView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        // Setup the "error" view
        errorView = inflate(context, errorLayoutResId, null)
        addView(errorView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        isInitialized = true
        updateState()
    }

    /**
     * Update the state of this view. This controls which views are visible.
     */
    private fun updateState() {
        if (!isInitialized) return

        // Update the error and no data texts
        errorView.findViewById<TextView>(R.id.text)?.text = errorText
        loadedNoDataView.findViewById<TextView>(R.id.text)?.text = noDataText

        // Update the views
        loadedView.visibility =
            if ((status == Result.Status.SUCCESS) && (hasData)) View.VISIBLE else View.GONE
        loadedNoDataView.visibility =
            if ((status == Result.Status.SUCCESS) && (!hasData)) View.VISIBLE else View.GONE
        loadingView.visibility = if (status == Result.Status.LOADING) View.VISIBLE else View.GONE
        errorView.visibility = if (status == Result.Status.ERROR) View.VISIBLE else View.GONE
    }
}