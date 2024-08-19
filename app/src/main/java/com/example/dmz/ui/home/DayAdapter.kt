package com.example.dmz.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dmz.R


class DayAdapter(private val days: List<Date>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    class DayViewHolder(view: View) : ViewHolder(view) {
        val dayOfWeek: TextView = view.findViewById(R.id.tv_day_of_week)
        val dayOfMonth: TextView = view.findViewById(R.id.tv_day_of_month)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val date = days[position]
        holder.dayOfWeek.text = date.dayOfWeek
        holder.dayOfMonth.text = date.dayOfMonth
    }

    override fun getItemCount() = days.size
}
