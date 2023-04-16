package com.example.phinconattendance.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.phinconattendance.databinding.LocationItemBinding
import com.example.phinconattendance.models.AttendanceModel
import java.util.*


class AttendanceInAdapter : RecyclerView.Adapter<AttendanceInAdapter.AttendanceViewHolder>() {

    private val getAttendance = ArrayList<AttendanceModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setHistory(location: ArrayList<AttendanceModel>) {
        getAttendance.clear()
        getAttendance.addAll(location)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttendanceViewHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val (_, _, img, loc_name, address, check_in, _) = getAttendance[position]
        holder.binding.locName.text =
            StringBuilder().append("Check In").append(" - ").append(loc_name).append(" - ")
                .append(check_in)
        holder.binding.locAddress.text = address

        Glide.with(holder.itemView.context)
            .load(img)
            .centerCrop()
            .into(holder.binding.locImg)
    }

    override fun getItemCount() = getAttendance.size

    class AttendanceViewHolder(var binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

    class AttendanceOutAdapter : RecyclerView.Adapter<AttendanceOutAdapter.AttendanceViewHolder>() {

        private val getAttendance = ArrayList<AttendanceModel>()

        @SuppressLint("NotifyDataSetChanged")
        fun setHistory(location: ArrayList<AttendanceModel>) {
            getAttendance.clear()
            getAttendance.addAll(location)
            notifyDataSetChanged()
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
            val binding =
                LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return AttendanceViewHolder(binding)
        }

        @Suppress("DEPRECATION")
        override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
            val (_, _, img, loc_name, address, _, check_out) = getAttendance[position]
            holder.binding.locName.text =
                StringBuilder().append("Check Out").append(" - ").append(loc_name).append(" - ")
                    .append(check_out)
            holder.binding.locAddress.text = address

            Glide.with(holder.itemView.context)
                .load(img)
                .centerCrop()
                .into(holder.binding.locImg)

        }

        override fun getItemCount() = getAttendance.size

        class AttendanceViewHolder(var binding: LocationItemBinding) :
            RecyclerView.ViewHolder(binding.root)
    }

