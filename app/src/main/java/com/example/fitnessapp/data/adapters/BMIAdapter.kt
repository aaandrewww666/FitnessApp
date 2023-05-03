package com.example.fitnessapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R

class BMIAdapter(private val textViewShowText: List<String>, private val textViewInfoShowText: List<String>): RecyclerView.Adapter<BMIAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewShow = itemView.findViewById<TextView>(R.id.textViewShow)
        val textViewInfo = itemView.findViewById<TextView>(R.id.textViewInfoShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_bmi, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textViewShow.text = textViewShowText[position]
        holder.textViewInfo.text = textViewInfoShowText[position]
    }

    override fun getItemCount(): Int = textViewShowText.size
}