package github.com.worker8.bidirectionviewpager

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

class HorizontalViewPagerAdapter(var biAdapter: BiDirectionViewPager.BiDirectionViewPagerAdapter?, val verticalPosition: Int) : PagerAdapter() {

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        biAdapter?.also { _biAdapter ->
            val viewHolder = _biAdapter.onCreateView(parent)
            val view = viewHolder.itemView
            parent.addView(view)
            _biAdapter.onBindView(viewHolder, verticalPosition, position)
            return view
        }
        return Unit
    }

    override fun destroyItem(parent: ViewGroup, position: Int, _object: Any) {
        parent.removeView(_object as View)
    }

    override fun isViewFromObject(view: View, _object: Any): Boolean {
        return view == _object
    }

    override fun getCount() = biAdapter?.getHorizontalItemCount(verticalPosition) ?: 0

    fun dispose() {
        biAdapter = null
        notifyDataSetChanged()
    }
}