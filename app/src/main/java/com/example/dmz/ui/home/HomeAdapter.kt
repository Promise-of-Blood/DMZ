package com.example.dmz.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dmz.R


class KeywordAdapter(private val imageList: List<Keyword>) : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keyword, parent, false)
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        val keyword = imageList[position]
        holder.bind(keyword)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class KeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_keyword_image)
        private val textView: TextView = itemView.findViewById(R.id.iv_keyword_text)

        // 데이터를 바인딩하는 함수
        fun bind(keyword: Keyword) {
            imageView.setImageResource(keyword.keyImage)
            textView.text = keyword.keyText
        }
    }
}