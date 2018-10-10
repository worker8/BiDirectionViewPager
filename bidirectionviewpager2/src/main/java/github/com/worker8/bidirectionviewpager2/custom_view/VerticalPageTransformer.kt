package github.com.worker8.bidirectionviewpager

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Taken from https://stackoverflow.com/a/22797619/75579
 * (comes with VerticalViewPager)
 */
class VerticalPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.alpha = 0f

        } else if (position <= 1) { // [-1,1]
            view.alpha = 1f

            // Counteract the default slide transition
            view.translationX = view.getWidth() * -position
            //set Y position to swipe in from top
            val yPosition = position * view.getHeight()
            view.translationY = yPosition

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.alpha = 0f
        }
    }
}

