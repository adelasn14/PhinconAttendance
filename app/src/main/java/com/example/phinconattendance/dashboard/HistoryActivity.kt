package com.example.phinconattendance.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phinconattendance.R
import com.example.phinconattendance.adapters.AttendanceInAdapter
import com.example.phinconattendance.adapters.AttendanceOutAdapter
import com.example.phinconattendance.databinding.ActivityHistoryBinding
import com.example.phinconattendance.session.UserSession
import com.example.phinconattendance.viewModels.LocationViewModel
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: AttendanceInAdapter
    private lateinit var viewModel: LocationViewModel
    private lateinit var attendanceOutAdapter : AttendanceOutAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        attendanceDay()
        attendanceWeek()
        attendanceMonth()
        attendanceYear()
        setupNavigation()

    }

    private fun attendanceDay() {
        binding.apply {
            btnDay.setOnClickListener {
                val todayDate = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))

                val username = UserSession(this@HistoryActivity).passUsername()
                Log.d("adapter", "today dan username: $todayDate, $username")

                adapter = AttendanceInAdapter()

                viewModel.getAttendanceTodayUser(username.toString(), todayDate)

                viewModel.allTodayAttendance.observe(this@HistoryActivity) {
                    if (it != null) {
                        adapter.setHistory(it)

                        binding.apply {
                            rvLocation.layoutManager = LinearLayoutManager(this@HistoryActivity)
                            rvLocation.setHasFixedSize(true)
                            rvLocation.adapter = adapter
                        }

                    } else {
                        Toast.makeText(this@HistoryActivity, "Let's get you check in first", Toast.LENGTH_SHORT).show()
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun attendanceWeek() {
        binding.apply {
            btnWeek.setOnClickListener {
                val timeZone = TimeZone.getTimeZone("Asia/Bangkok")

                val calendar = Calendar.getInstance(timeZone)

                //get first date of the week
                val startOfWeek = calendar.apply {
                    firstDayOfWeek = Calendar.MONDAY
                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                }.time

                val todayDate = SimpleDateFormat("yyyy-MM-dd").format(startOfWeek)

                //get last day of the week
                val endOfWeek = calendar.apply {
                    firstDayOfWeek = Calendar.MONDAY
                    set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                }.time
                val nextWeekDate = SimpleDateFormat("yyyy-MM-yy").format(endOfWeek)


                val username = UserSession(this@HistoryActivity).passUsername()

                Log.d("history activity", "week dan username: $todayDate, $todayDate, $username")

                adapter = AttendanceInAdapter()

                viewModel.getAttendanceWeekUser(todayDate, nextWeekDate, username.toString())

                viewModel.allWeekAttendance.observe(this@HistoryActivity) {
                    if (it != null) {
                        adapter.setHistory(it)

                        Log.d("display history", "isi week history: $it")

                        binding.apply {
                            rvLocation.layoutManager = LinearLayoutManager(this@HistoryActivity)
                            rvLocation.setHasFixedSize(true)
                            rvLocation.adapter = adapter
                        }

                    } else {
                        Toast.makeText(this@HistoryActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun attendanceMonth() {
        binding.apply {
            btnMonth.setOnClickListener {
                val todayDate = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))

                val calendar = Calendar.getInstance()

                calendar.add(Calendar.DATE, 30)
                val nextMonthDate = SimpleDateFormat("yyyy-MM-yy").format(calendar.time)

                val username = UserSession(this@HistoryActivity).passUsername()

                Log.d("history activity", "month dan username: $todayDate, $nextMonthDate, $username")

                adapter = AttendanceInAdapter()

                viewModel.getAttendanceMonthUser(todayDate, nextMonthDate, username.toString())

                viewModel.allMonthAttendance.observe(this@HistoryActivity) {
                    if (it != null) {
                        adapter.setHistory(it)

                        Log.d("display history", "isi month history: $it")

                        binding.apply {
                            rvLocation.layoutManager = LinearLayoutManager(this@HistoryActivity)
                            rvLocation.setHasFixedSize(true)
                            rvLocation.adapter = adapter
                        }

                    } else {
                        Toast.makeText(this@HistoryActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun attendanceYear() {
        binding.apply {
            btnYear.setOnClickListener {
                val todayDate = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))

                val calendar = Calendar.getInstance()

                calendar.add(Calendar.DATE, 365)
                val nextYearDate = SimpleDateFormat("yyyy-MM-yy").format(calendar.time)

                val username = UserSession(this@HistoryActivity).passUsername()

                Log.d("history activity", "year dan username: $todayDate, $nextYearDate, $username")

                adapter = AttendanceInAdapter()

                viewModel.getAttendanceYearUser(todayDate, nextYearDate, username.toString())

                viewModel.allYearAttendance.observe(this@HistoryActivity) {
                    if (it != null) {
                        adapter.setHistory(it)

                        Log.d("display history", "isi year history: $it")

                        binding.apply {
                            rvLocation.layoutManager = LinearLayoutManager(this@HistoryActivity)
                            rvLocation.setHasFixedSize(true)
                            rvLocation.adapter = adapter
                        }

                    } else {
                        Toast.makeText(this@HistoryActivity, "No data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LocationViewModel::class.java]
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_history

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_history -> {
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }
}