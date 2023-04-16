package com.example.phinconattendance.session

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.phinconattendance.R


class UserSession(context: Context) {
    companion object {
        private const val ID_KEY = "userID"
        private const val USN_KEY = "usn"
    }

    private val loginSession =
        context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)


    fun saveUsername(username: String) {
        val editor = loginSession.edit()
        editor.putString(USN_KEY, username)
        editor.apply()
    }

    fun passUsername(): String? {
        return loginSession.getString(USN_KEY, "USN")
    }
}