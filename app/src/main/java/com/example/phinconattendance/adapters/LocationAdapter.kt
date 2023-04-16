package com.example.phinconattendance.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phinconattendance.databinding.LocationItemBinding
import com.example.phinconattendance.models.LocationModel

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private val getLocation = ArrayList<LocationModel>()
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var selected_position = -1

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLocation(location: ArrayList<LocationModel>) {
        getLocation.clear()
        getLocation.addAll(location)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.apply {
            binding.apply {
                val (img, loc_name, address) = getLocation[position]

                Glide.with(itemView.context)
                    .load(img)
                    .centerCrop()
                    .into(locImg)

                locName.text = loc_name
                locAddress.text = address

                if (selected_position == position) {
                    cardViewLoc.setBackgroundColor(Color.parseColor("#0063C6"))
                    locName.setTextColor(Color.parseColor("#FFFFFF"))
                    locAddress.setTextColor(Color.parseColor("#FFFFFF"))
                } else {
                    cardViewLoc.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    locName.setTextColor(Color.parseColor("#000000"))
                    locAddress.setTextColor(Color.parseColor("#000000"))
                }

                itemView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickCallback.onItemClicked(getLocation[position])
                        selected_position = position
                        notifyDataSetChanged()
                    }
                }
            }
        }

    }

    override fun getItemCount() = getLocation.size

    class LocationViewHolder(var binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: LocationModel)
    }
}
