package com.example.dmz.ui.home

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.R


class ImagePagerAdapter(private val imageList: List<Int>, private val context: Context) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            scaleType = ImageView.ScaleType.CENTER_CROP

            background = context.getDrawable(R.drawable.shape_corner_radius_30dp)

            clipToOutline = true

        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val actualPosition = position % imageList.size
        holder.imageView.setImageResource(imageList[actualPosition])
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE // 무한 스크롤을 위해 크게 설정
    }

    class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}
