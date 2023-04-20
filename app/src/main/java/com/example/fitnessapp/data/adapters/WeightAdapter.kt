package com.example.fitnessapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.api.requests.WeightRequest
import java.util.*

class WeightAdapter(private val itemList : List<WeightRequest>) : RecyclerView.Adapter<WeightAdapter.MyViewHolder>() {
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
        val weight = itemList[position].userWeight
        val date = itemList[position].date
        if(Locale.getDefault().country != "RU") {
            holder.weightShow.text = "Weight: $weight, Date: $date"
        } else {
            holder.weightShow.text = "Вес: $weight, Дата: $date"
        }
    }

    override fun getItemCount(): Int = itemList.size
}