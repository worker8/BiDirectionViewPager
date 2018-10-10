package github.com.worker8.bidirectionviewpager

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_view.view.*

class MainAdapter(val data: List<Pair<String, List<String>>>) : BiDirectionViewPager.BiDirectionViewPagerAdapter {
    val REPEAT_COUNT = 30

    override fun onCreateView(parent: ViewGroup): BiDirectionViewPager.ViewHolder {
        return VideoViewHolder.create(parent)
    }

    override fun onBindView(viewHolder: BiDirectionViewPager.ViewHolder, rowPosition: Int, columnPosition: Int) {
        val (groupName, rowData) = data[rowPosition]
        val originalSize = rowData.size
        val url = rowData[columnPosition.rem(originalSize)]

        (viewHolder as VideoViewHolder).itemView.apply {
            textView.text = groupName
            Picasso.get().load(url).resize(0, resources.displayMetrics.heightPixels).into(imageView)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getHorizontalItemCount(verticalPosition: Int): Int {
        return data[verticalPosition].second.size * REPEAT_COUNT
    }

    class VideoViewHolder(itemView: View) : BiDirectionViewPager.ViewHolder(itemView) {
        companion object {
            fun create(parent: ViewGroup): VideoViewHolder {
                return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))
            }
        }
    }
}

