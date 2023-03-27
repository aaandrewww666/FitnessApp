package com.example.fitnessapp.ui.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.ui.data.WeightData
import com.example.fitnessapp.ui.data.adapters.WeightAdapter.*
import java.util.*

class WeightAdapter(private val itemList : List<WeightData>) : RecyclerView.Adapter<MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weightShow : TextView = itemView.findViewById(R.id.weight_TV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.weight_rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val weight = itemList[position].weight
        val date = itemList[position].data
        if(Locale.getDefault().country != "RU") {
            holder.weightShow.text = "Weight: $weight, Date: $date"
        } else {
            holder.weightShow.text = "Вес: $weight, Дата: $date"
        }
    }

    override fun getItemCount(): Int = itemList.size
}