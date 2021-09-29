package com.trigonated.gamecollection.ui.misc

import android.content.Context
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.trigonated.gamecollection.R

/**
 * [CoordinatorLayout] layout for a card containing the image of a game.
 *
 * This card has behaviour similar to a fab, hiding when it "hits" the Toolbar.
 */
class GameImageCardBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<CardView>(context, attrs) {
    /** Animation for when the CardView is being hidden. */
    private var hideAnim: Animation? = null

    /** Animation for when the CardView is being showed. */
    private var showAnim: Animation? = null

    /** Auxiliary rect used for calculations. */
    private var auxRect: Rect? = null

    /** Auxiliary matrix used for calculations. */
    private val matrix = ThreadLocal<Matrix>()

    /** Auxiliary rect used for calculations. */
    private val rectF = ThreadLocal<RectF>()

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: CardView,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout) {
            // Show/hide the card automatically when depending on an AppBarLayout
            updateCardVisibilityForAppBarLayout(parent, (dependency as AppBarLayout?)!!, child)
        }
        return false
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: CardView,
        layoutDirection: Int
    ): Boolean {
        // Update visibility from the dependencies
        val dependencies: List<View> = parent.getDependencies(child)
        for (dependency: View in dependencies) {
            if (dependency is AppBarLayout) {
                if (updateCardVisibilityForAppBarLayout(parent, dependency, child)) {
                    break
                }
            }
        }
        // Let the CoordinatorLayout layout the child
        parent.onLayoutChild(child, layoutDirection)
        return true
    }

    /**
     * Hides the card, playing an animation in the process.
     */
    private fun hideCardView(view: CardView) {
        // Setup the animation, if needed
        if (hideAnim == null) {
            hideAnim = AnimationUtils.loadAnimation(view.context, R.anim.hide_gameimagecard)
            hideAnim!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    view.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        // Start the animation
        if ((view.visibility == View.VISIBLE) && (view.animation != hideAnim)) {
            view.clearAnimation()
            view.startAnimation(hideAnim!!)
        }
    }

    /**
     * Shows the card, playing an animation in the process.
     */
    private fun showCardView(view: CardView) {
        // Setup the animation, if needed
        if (showAnim == null) {
            showAnim = AnimationUtils.loadAnimation(view.context, R.anim.show_gameimagecard)
            showAnim!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {}

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        // Start the animation
        if ((view.visibility != View.VISIBLE) && (view.animation != showAnim)) {
            view.clearAnimation()
            view.startAnimation(showAnim!!)
        }
    }

    /**
     * Hides/shows the card, depending on the AppBarLayout it depends on.
     */
    private fun updateCardVisibilityForAppBarLayout(
        parent: CoordinatorLayout,
        appBarLayout: AppBarLayout,
        child: CardView
    ): Boolean {
        if (auxRect == null) auxRect = Rect()

        // Get the visible rect of the dependency
        val rect: Rect = auxRect!!
        getDescendantRect(parent, appBarLayout, rect)
        // Hide/show the card depending on whether the card is "touching" the AppBar
        if (rect.bottom <= appBarLayout.minimumHeightForVisibleOverlappingContent + child.translationY) {
            hideCardView(child)
        } else {
            showCardView(child)
        }
        return true
    }

    private fun getDescendantRect(parent: ViewGroup, descendant: View, out: Rect) {
        out[0, 0, descendant.width] = descendant.height
        offsetDescendantRect(parent, descendant, out)
    }

    private fun offsetDescendantRect(parent: ViewGroup, descendant: View, rect: Rect) {
        var m = matrix.get()
        if (m == null) {
            m = Matrix()
            matrix.set(m)
        } else {
            m.reset()
        }
        offsetDescendantMatrix(parent, descendant, m)
        var rectF = rectF.get()
        if (rectF == null) {
            rectF = RectF()
            rectF.set(rectF)
        }
        rectF.set(rect)
        m.mapRect(rectF)
        rect[
                (rectF.left + 0.5f).toInt(),
                (rectF.top + 0.5f).toInt(),
                (rectF.right + 0.5f).toInt()
        ] = (rectF.bottom + 0.5f).toInt()
    }

    private fun offsetDescendantMatrix(target: ViewParent, view: View, m: Matrix) {
        val parent = view.parent
        if (parent is View && parent !== target) {
            val vp = parent as View
            offsetDescendantMatrix(target, vp, m)
            m.preTranslate(-vp.scrollX.toFloat(), -vp.scrollY.toFloat())
        }
        m.preTranslate(view.left.toFloat(), view.top.toFloat())
        if (!view.matrix.isIdentity) {
            m.preConcat(view.matrix)
        }
    }
}