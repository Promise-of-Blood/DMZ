package com.example.dmz.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dmz.R
import com.example.dmz.data.model.Keywords
import com.example.dmz.databinding.ItemCalendarDayBinding

//TODO keywordsList[X] Keywords 객체 하나만 받아오기
class DayAdapter(private val days: List<Date>, private val keywordsList: List<Keywords>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    class DayViewHolder(val binding: ItemCalendarDayBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val date = days[position]

        val statusColor = when(keywordsList[0].recentTrend[position]) {
            in 4..5 -> R.color.sky_blue
            in 2..3 -> R.color.flou_yellow
            else -> R.color.light_gray
        }
        holder.binding.apply {
            tvDayOfWeek.text = date.dayOfWeek
            tvDayOfMonth.text = date.dayOfMonth
            ivStatus.setColorFilter(ContextCompat.getColor(root.context, statusColor))
        }
    }

    override fun getItemCount() = days.size
}
