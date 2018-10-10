package github.com.worker8.bidirectionviewpager

import android.content.Context
import android.graphics.Point
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import github.com.worker8.bidirectionviewpager2.R
import kotlinx.android.synthetic.main.horizontal_view_pager.view.*

/**
 * This is created based off this answer in SO: https://stackoverflow.com/a/22797619/75579
 * It supports scrolling left right and also up and down for view pager.
 *
 * So it works with BiDirectionViewPagerAdapter which is written based on RecyclerView
 * set `biAdapter` instead of `adapter`
 */

class BiDirectionViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    var biAdapter: BiDirectionViewPagerAdapter? = null
        set(value) {
            value?.let {
                field = it
                adapter = BiDirectionViewPagerAdapterInternal(it)
            }
        }

    val FINGER_MOVE_THRESHOLD = 30 // pixels, need to use dp later on
    var initialTouchPoint = Point(0, 0)
    var firstTime = true

    init {
        setPageTransformer(true, VerticalPageTransformer()) // VerticalPageTransformer makes ViewPager move vertically
        // overScrollMode = View.OVER_SCROLL_NEVER // TODO: re-enable, disable for now, so that the last page can be seen clearly
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val currentPoint = Point(event.x.toInt(), event.y.toInt())

        if (action == MotionEvent.ACTION_DOWN) {
            // mark the beginning, when finger touched down
            initialTouchPoint = Point(currentPoint)
        } else if (action == MotionEvent.ACTION_UP) {
            // reset the marking, when finger is lifted up
            initialTouchPoint = Point(0, 0)
        } else {
            val moveDistance = currentPoint.distanceFrom(initialTouchPoint)
            if (moveDistance > FINGER_MOVE_THRESHOLD) {
                val direction = MotionUtil.getDirection(initialTouchPoint, currentPoint)
                // check if the scrolling is vertical
                if (direction == MotionUtil.Direction.up || direction == MotionUtil.Direction.down) {
                    return true
                }
            }
        }

        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // swapping the motionEvent's x and y, so that when finger moves right, it becomes moving down
        // for VerticalViewPager effect
        event.swapXY()

        // this portion is used for injection ACTION_DOWN
        if (firstTime && event.actionMasked == MotionEvent.ACTION_MOVE) {
            injectActionDown(event)
            firstTime = false
        }
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            firstTime = true
        }
        super.onTouchEvent(event)
        return true
    }

    /**
     * in order for super.onTouchEvent() to actually scroll the VerticalViewPager,
     * we need to start with ACTION_DOWN
     *
     * Because the complete cycle is:
     * super.onTouchEvent(event with ACTION_DOWN) x 1 time
     * super.onTouchEvent(event with ACTION_MOVE) x n times
     * super.onTouchEvent(event with ACTION_DOWN) x 1 time
     *
     * this method returns nothing, but has side effect
     */
    private fun injectActionDown(event: MotionEvent) {
        event.action = MotionEvent.ACTION_DOWN
        super.onTouchEvent(event)
        event.action = MotionEvent.ACTION_MOVE
    }

    /**
     * when there is data change in BiDirectionViewPager itself
     */
    fun notifyDataSetChanged() {
        adapter?.notifyDataSetChanged()
    }

    /**
     * when there is data change in one of the HorizontalViewPager row
     */
    fun notifyDataSetChangedHorizontal(verticalPosition: Int) {
        (adapter as? BiDirectionViewPagerAdapterInternal)?.let {
            it.viewList[verticalPosition]?.horizontalViewPager?.adapter?.notifyDataSetChanged()
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private fun MotionEvent.swapXY() {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = y / height * width
        val newY = x / width * height
        setLocation(newX, newY)
    }

    interface BiDirectionViewPagerAdapter {
        fun onCreateView(parent: ViewGroup): ViewHolder
        fun onBindView(viewHolder: ViewHolder, rowPosition: Int, columnPosition: Int)
        fun getItemCount(): Int
        fun getHorizontalItemCount(verticalPosition: Int): Int
    }

    open class ViewHolder(val itemView: View)
}

private class BiDirectionViewPagerAdapterInternal(val biAdapter: BiDirectionViewPager.BiDirectionViewPagerAdapter) : PagerAdapter() {
    val viewList = mutableMapOf<Int, View>()

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_view_pager, parent, false)
        parent.addView(view)
        view.apply {
            horizontalViewPager.adapter = HorizontalViewPagerAdapter(biAdapter, position)
        }

        viewList[position] = view

        return view
    }

    override fun destroyItem(parent: ViewGroup, position: Int, _object: Any) {
        val view = _object as View
        view.apply {
            (horizontalViewPager.adapter as HorizontalViewPagerAdapter).dispose()
            horizontalViewPager.adapter = null
        }
        parent.removeView(view)

        viewList.remove(position)
    }

    override fun isViewFromObject(view: View, _object: Any): Boolean {
        val _view = (_object as View)
        _view.horizontalViewPager
        return view == _view
    }

    override fun getCount() = biAdapter.getItemCount()
}