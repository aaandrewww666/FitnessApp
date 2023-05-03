package com.example.fitnessapp.data.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.fitnessapp.R

class ExercisePicturesAdapter(private val itemList : List<String>): RecyclerView.Adapter<ExercisePicturesAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewPicture: ImageView = itemView.findViewById(R.id.imageView_exercise)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_exercise_pictures, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val circularProgressDrawable = CircularProgressDrawable(holder.imageViewPicture.context)
        circularProgressDrawable.setColorSchemeColors(Color.BLACK)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()

        Glide
            .with(holder.imageViewPicture.context)
            .load(itemList[position])
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .into(holder.imageViewPicture)
    }

    override fun getItemCount(): Int = itemList.size
}