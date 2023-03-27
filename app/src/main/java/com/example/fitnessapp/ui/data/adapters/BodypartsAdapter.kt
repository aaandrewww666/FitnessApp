package com.example.fitnessapp.ui.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R

class BodypartsAdapter(private val itemList : List<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<BodypartsAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun clickListener(item : String)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonNext: Button = itemView.findViewById(R.id.button_rv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.buttonNext.text = itemList[position]
        holder.buttonNext.setOnClickListener{
            listener.clickListener(itemList[position])
        }
    }

    override fun getItemCount(): Int = itemList.size
}