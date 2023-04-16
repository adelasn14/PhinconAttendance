package com.example.phinconattendance.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phinconattendance.databinding.SliderItemBinding
import com.example.phinconattendance.models.SliderModel

class ImageSliderAdapter(private val imageList: ArrayList<SliderModel>) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: SliderItemBinding): RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        fun bind(data: SliderModel) {
            with(binding){
                Glide.with(itemView)
                    .load(data.url)
                    .into(imageView)
                titleText.text = data.title
                descText.text = data.text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }
}