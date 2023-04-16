package com.example.phinconattendance.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.phinconattendance.models.AttendanceModel
import com.example.phinconattendance.models.LocationModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LocationViewModel : ViewModel() {
    private val _allLocation = MutableLiveData<ArrayList<LocationModel>>()
    val getLocation: LiveData<ArrayList<LocationModel>> = _allLocation

    fun getLocation() {
        val locs = arrayListOf<LocationModel>()

        Firebase.database.getReference("Location").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children) {

                    val loc = h.getValue(LocationModel::class.java)

                    locs.add(loc!!)

                    _allLocation.value = locs
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("locs retrieved", "locations : $_allLocation")

            }
        })
    }


    // from check_in
    private val _checkInAttendance = MutableLiveData<ArrayList<AttendanceModel>>()
    val checkInAttendance: LiveData<ArrayList<AttendanceModel>> = _checkInAttendance

    fun getAttendanceCheckInUser(username : String, attendanceID: String) {
        val todays = arrayListOf<AttendanceModel>()

        FirebaseDatabase.getInstance().getReference("Attendance").child(username).child(attendanceID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(AttendanceModel::class.java)

                todays.add(data!!)

                Log.d("check in attendance vm", "attendance setelah masuk model : $todays")

                _checkInAttendance.value = todays
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("today attendance vm", "attendance : $_checkInAttendance")
            }

        })
    }

    private val _allTodayAttendance = MutableLiveData<ArrayList<AttendanceModel>>()
    val allTodayAttendance: LiveData<ArrayList<AttendanceModel>> = _allTodayAttendance


    // from today
    fun getAttendanceTodayUser(username : String, todayDate: String) {
        val todays = arrayListOf<AttendanceModel>()

        FirebaseDatabase.getInstance().getReference("Attendance").child(username)
            .orderByChild("date").startAt(todayDate).endAt(todayDate).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children) {
                    val data = h.getValue(AttendanceModel::class.java)

                    Log.d("today attendance vm", "attendance sebelum masuk model : $data")

                    todays.add(data!!)

                    Log.d("today attendance vm", "attendance setelah masuk model : $todays")

                    _allTodayAttendance.value = todays
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("today attendance vm", "attendance : $_allTodayAttendance")
            }

        })
    }

    // from week
    private val _allWeekAttendance = MutableLiveData<ArrayList<AttendanceModel>>()
    val allWeekAttendance: LiveData<ArrayList<AttendanceModel>> = _allWeekAttendance

    fun getAttendanceWeekUser(todayDate : String, nextWeekDate: String, username: String) {
        val weeks = arrayListOf<AttendanceModel>()

        FirebaseDatabase.getInstance().reference.child("Attendance").child(username).orderByChild("date")
            .startAt(todayDate).endAt(nextWeekDate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children) {
                    val week = h.getValue(AttendanceModel::class.java)

                    Log.d("user weekly attendance", "sebelum masuk model attendance : $week")

                    weeks.add(week!!)

                    _allWeekAttendance.value = weeks

                    Log.d("user weekly attendance", "sesudah masuk model attendance : $weeks")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("locs retrieved", "locations : $_allWeekAttendance")

            }
        })
    }


    // from month
    private val _allMonthAttendance = MutableLiveData<ArrayList<AttendanceModel>>()
    val allMonthAttendance: LiveData<ArrayList<AttendanceModel>> = _allMonthAttendance

    fun getAttendanceMonthUser(todayDate : String, nextMonthDate: String, username: String) {
        val weeks = arrayListOf<AttendanceModel>()

        FirebaseDatabase.getInstance().reference.child("Attendance").child(username).orderByChild("date").startAt(todayDate).endAt(nextMonthDate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children) {
                    if (h != null) {
                        val week = h.getValue(AttendanceModel::class.java)

                        weeks.add(week!!)

                        _allMonthAttendance.value = weeks

                        Log.d("user monthly attendance vm", "data : $weeks")
                    }
                    else {
                        Log.d("user monthly attendance vm", "No data")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("locs retrieved", "locations : $_allMonthAttendance")

            }
        })
    }


    // from year
    private val _allYearAttendance = MutableLiveData<ArrayList<AttendanceModel>>()
    val allYearAttendance: LiveData<ArrayList<AttendanceModel>> = _allYearAttendance

    fun getAttendanceYearUser(todayDate : String, nextYearDate: String, username: String) {
        val weeks = arrayListOf<AttendanceModel>()

        FirebaseDatabase.getInstance().reference.child("Attendance").child(username).orderByChild("date")
            .startAt(todayDate).endAt(nextYearDate).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children) {
                    if (h != null) {
                        val week = h.getValue(AttendanceModel::class.java)

                        weeks.add(week!!)

                        _allYearAttendance.value = weeks

                        Log.d("user annually attendance vm", "data : $weeks")
                    }
                    else {
                        Log.d("user annually attendance vm", "No data")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("locs retrieved", "locations : $_allYearAttendance")

            }
        })
    }
}