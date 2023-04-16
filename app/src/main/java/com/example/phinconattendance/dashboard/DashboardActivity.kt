package com.example.phinconattendance.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phinconattendance.R
import com.example.phinconattendance.adapters.AttendanceInAdapter
import com.example.phinconattendance.adapters.LocationAdapter
import com.example.phinconattendance.databinding.ActivityDashboardBinding
import com.example.phinconattendance.models.AttendanceModel
import com.example.phinconattendance.models.LocationModel
import com.example.phinconattendance.session.AttendanceSession
import com.example.phinconattendance.session.UserSession
import com.example.phinconattendance.viewModels.LocationViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var attendanceInAdapter: AttendanceInAdapter
    private lateinit var database: DatabaseReference
    private lateinit var viewModel: LocationViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("Attendance")

        setupViewModel()
        setupAdapter()
        doCheckIn()
        setupNavigation()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupAdapter() {
        locationAdapter = LocationAdapter()

        viewModel.getLocation()
        viewModel.getLocation.observe(this) {
            if (it != null) {
                locationAdapter.setLocation(it)

                binding.apply {
                    rvLocation.layoutManager = LinearLayoutManager(this@DashboardActivity)
                    rvLocation.setHasFixedSize(true)
                    rvLocation.adapter = locationAdapter
                }

                locationAdapter.notifyDataSetChanged()

            } else {
               Toast.makeText(this, "Location is not exist", Toast.LENGTH_SHORT).show()
            }
        }

        locationAdapter.setOnItemClickCallback(object : LocationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: LocationModel) {

            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LocationViewModel::class.java]
    }

    private fun doCheckIn() {
        binding.apply {
            checkButtonIn.visibility = View.VISIBLE
            locationAdapter.setOnItemClickCallback(object : LocationAdapter.OnItemClickCallback {
                override fun onItemClicked(data: LocationModel) {
                    val today = Date(System.currentTimeMillis())
                    val todayDate = SimpleDateFormat("yyyy-MM-dd").format(today)

                    val session = AttendanceSession(this@DashboardActivity)
                    session.saveDate(todayDate)
                    session.saveAddress(data.address.toString())
                    session.saveImg(data.img.toString())
                    session.saveLocName(data.loc_name.toString())
                }
            })

            val sessUser = UserSession(this@DashboardActivity)
            val username = sessUser.passUsername()

            val session = AttendanceSession(this@DashboardActivity)
            val date = session.passDate()
            val img = session.passImg()
            val loc_name = session.passLocName()
            val address = session.passAddress()

            val id : String? = database.child(username.toString()).push().key
            session.saveID(id.toString())

            val today = Date(System.currentTimeMillis())
            val todayHour = SimpleDateFormat("hh:mm a").format(today)

            checkButtonIn.setOnClickListener {
                checkButtonIn.visibility = View.GONE
                val todayAttendance = AttendanceModel(id, date, img, loc_name, address, todayHour)
                database.child(username.toString()).child(id.toString()).setValue(todayAttendance).addOnSuccessListener {
                    Log.d("getting who's checking in now", "$id, $username at $date $todayHour")

                    AlertDialog.Builder(this@DashboardActivity).apply {
                        setTitle("Yeah!")
                        setMessage("You checked in!")

                        attendanceInAdapter = AttendanceInAdapter()

                        viewModel.getAttendanceCheckInUser(username.toString(), id.toString())

                        viewModel.checkInAttendance.observe(this@DashboardActivity) {
                            if (it != null) {
                                attendanceInAdapter.setHistory(it)

                                rvLocation.layoutManager = LinearLayoutManager(this@DashboardActivity)
                                rvLocation.setHasFixedSize(true)
                                rvLocation.adapter = attendanceInAdapter

                                checkButtonOut.visibility = View.VISIBLE

                            } else {
                                Toast.makeText(this@DashboardActivity, "Let's get you check in first", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this@DashboardActivity, "Failed to check in.", Toast.LENGTH_SHORT).show()
                    }
            }
            doCheckOut()
        }

    }

    private fun doCheckOut() {
        val sessUser = UserSession(this@DashboardActivity)
        val username = sessUser.passUsername()

        val session = AttendanceSession(this@DashboardActivity)
        val date = session.passDate()

        val id = session.passID()

        val today = Date(System.currentTimeMillis())
        val todayHour = SimpleDateFormat("hh:mm a").format(today)

        binding.apply {
            checkButtonOut.setOnClickListener {
                checkButtonOut.visibility = View.VISIBLE
                database.child(username.toString()).child(id.toString()).child("check_out").setValue(todayHour)
                    .addOnSuccessListener {
                        Log.d("getting who's checking out now", "$id, $username at $date $todayHour")

                        AlertDialog.Builder(this@DashboardActivity).apply {
                            setTitle("Yeah!")
                            setMessage("You checked out!")
                            setPositiveButton("Continue") { _, _ ->
                                val intent = Intent(context, HistoryActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Failed to check in.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            checkButtonOut.visibility = View.GONE
        }
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_history -> {
                    startActivity(Intent(applicationContext, HistoryActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}