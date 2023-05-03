package com.example.fitnessapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.models.PlanarClassification

class ClassificationAdapter(private val itemList : List<PlanarClassification>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ClassificationAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun clickListener(item : PlanarClassification)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonNext: Button = itemView.findViewById(R.id.button_rv)
        val classificationTV: TextView = itemView.findViewById(R.id.classificationTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_classification, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.classificationTV.text = itemList[position].name
        holder.descriptionTV.text = itemList[position].description

        holder.buttonNext.setOnClickListener{
            listener.clickListener(itemList[position])
        }
    }

    override fun getItemCount(): Int = itemList.size
}