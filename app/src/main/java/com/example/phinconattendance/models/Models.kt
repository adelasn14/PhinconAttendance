package com.example.phinconattendance.models


data class SliderModel(
    val text: String? = "",
    val title: String? = "",
    val url: String? = ""
)

data class UserCredentialModel(
    val name: String? = "",
    val username: String? = "",
    val password: String? = "",
    val nik: String? = "",
    val address: String? = "",
    val occupation: String? = "",
    val img: String? = ""
)

data class LocationModel(
    val img: String? = "",
    val loc_name: String? = "",
    val address: String? = ""
)

data class AttendanceModel(
    val attendanceID: String? ="",
    val date: String? ="",
    val img: String? = "",
    val loc_name: String? = "",
    val address: String? = "",
    val check_in: String? = "",
    val check_out: String? = ""
)