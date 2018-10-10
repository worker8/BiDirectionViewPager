package github.com.worker8.bidirectionviewpager

import android.graphics.Point

/**
 * Use hypotenuse to calculate distanceFrom between 2 points
 */
fun Point.distanceFrom(point: Point): Int {
    val diffX = x - point.x
    val diffY = y - point.y
    return Math.hypot(diffX.toDouble(), diffY.toDouble()).toInt()
}

/**
 * syntax sugar to make deep copy
 */
fun Point.copy(point: Point): Point {
    return Point(point)
}