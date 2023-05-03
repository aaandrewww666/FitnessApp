package com.example.fitnessapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.data.models.Exercises
import org.w3c.dom.Text

class ExercisesAdapter(private val itemList : List<Exercises>) : RecyclerView.Adapter<ExercisesAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvPictures: RecyclerView = itemView.findViewById(R.id.recyclerViewPictures)
        val exerciseNameTV: TextView = itemView.findViewById(R.id.exerciseNameTV)
        val inventoryTV: TextView = itemView.findViewById(R.id.inventoryTV)
        val variantsTV: TextView = itemView.findViewById(R.id.variantsTV)
        val coreMusclesTV: TextView = itemView.findViewById(R.id.coreMusclesTV)
        val additionalMusclesTV: TextView = itemView.findViewById(R.id.additionalMusclesTV)
        val stabilizingMusclesTV: TextView = itemView.findViewById(R.id.stabilizingMusclesTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_exercise, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.exerciseNameTV.text = itemList[position].exerciseName
        holder.inventoryTV.text = itemList[position].inventory
        holder.variantsTV.text = itemList[position].variants
        holder.coreMusclesTV.text = itemList[position].coreMuscles
        holder.additionalMusclesTV.text = itemList[position].additionalMuscles
        holder.stabilizingMusclesTV.text = itemList[position].stabilizingMuscles
        holder.rvPictures.adapter = ExercisePicturesAdapter(itemList[position].photos)

    }

    override fun getItemCount(): Int = itemList.size
}