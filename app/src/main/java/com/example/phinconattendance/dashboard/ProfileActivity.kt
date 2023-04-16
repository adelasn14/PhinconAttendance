package com.example.phinconattendance.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.phinconattendance.R
import com.example.phinconattendance.databinding.ActivityProfileBinding
import com.example.phinconattendance.models.UserCredentialModel
import com.example.phinconattendance.session.UserSession
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("Users")

        getProfileData()
        setupNavigation()
    }

    private fun getProfileData() {
        binding.apply {
            val userSession = UserSession(this@ProfileActivity)
            val currUser = userSession.passUsername()
            Log.d(
                "LoginActivity",
                "ID : Username : ${userSession.passUsername()}"
            )

            database.orderByChild("username").equalTo(currUser)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (user in snapshot.children) {
                            // do something with the individual "issues"
                            val userCred = user.getValue(UserCredentialModel::class.java)

                            Glide.with(applicationContext)
                                .load(userCred?.img)
                                .centerCrop()
                                .into(userImg)

                            userFullname.text = userCred?.name.toString()
                            userOccupation.text = userCred?.occupation.toString()
                            noKaryawan.text = userCred?.nik.toString()
                            alamat.text = userCred?.address.toString()
                            changePass.text = userCred?.password.toString()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast
                            .makeText(
                                this@ProfileActivity,
                                "Username/Password is wrong",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }

                })
        }
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, DashboardActivity::class.java))
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