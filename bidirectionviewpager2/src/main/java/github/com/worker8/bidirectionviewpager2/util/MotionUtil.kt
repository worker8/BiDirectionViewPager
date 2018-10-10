package github.com.worker8.bidirectionviewpager

import android.graphics.Point
import android.util.Log

/**
 * This class calculates the direction of 2 coordinates
 * Let's say (0,0) to (0,100), it's a swipe down
 *
 * Taken from https://stackoverflow.com/a/26387629
 */

object MotionUtil {

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float): Direction {
        val angle = getAngle(x1, y1, x2, y2)
        return Direction.fromAngle(angle)
    }

    fun getDirection(point1: Point, point2: Point): Direction {
        val angle = getAngle(point1.x.toFloat(), point1.y.toFloat(), point2.x.toFloat(), point2.y.toFloat())
        return Direction.fromAngle(angle)
    }

    /**
     *
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {

        val rad = Math.atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()) + Math.PI
        return (rad * 180 / Math.PI + 180) % 360
    }


    enum class Direction {
        up,
        down,
        left,
        right;

        companion object {

            /**
             * Returns a direction given an angle.
             * Directions are defined as follows:
             *
             * Up: [45, 135]
             * Right: [0,45] and [315, 360]
             * Down: [225, 315]
             * Left: [135, 225]
             *
             * @param angle an angle from 0 to 360 - e
             * @return the direction of an angle
             */
            fun fromAngle(angle: Double): Direction {
                return if (inRange(angle, 45f, 135f)) {
                    Direction.up
                } else if (inRange(angle, 0f, 45f) || inRange(angle, 315f, 360f)) {
                    Direction.right
                } else if (inRange(angle, 225f, 315f)) {
                    Direction.down
                } else {
                    Direction.left
                }
            }

            /**
             * @param angle an angle
             * @param init the initial bound
             * @param end the final bound
             * @return returns true if the given angle is in the interval [init, end).
             */
            private fun inRange(angle: Double, init: Float, end: Float): Boolean {
                return angle >= init && angle < end
            }
        }
    }


}
