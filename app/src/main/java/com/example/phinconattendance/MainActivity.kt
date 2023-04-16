package com.example.phinconattendance

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.phinconattendance.adapters.ImageSliderAdapter
import com.example.phinconattendance.authenticationPage.LoginActivity
import com.example.phinconattendance.authenticationPage.SignupActivity
import com.example.phinconattendance.databinding.ActivityMainBinding
import com.example.phinconattendance.models.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: ImageSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("MainImageSlider")

        setupAnimation()
        getAutoSliderImage()
        intentToAuth()

    }

    private fun getAutoSliderImage() {
        binding.apply {

            val images = arrayListOf<SliderModel>()

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children) {
                        val image = h.getValue(SliderModel::class.java)

                        images.add(image!!)

                        Log.d("title text", "imageslider : $images")

                        adapter = ImageSliderAdapter(images)
                    }
                    viewPager.adapter = adapter
                }


                override fun onCancelled(error: DatabaseError) {
                    Toast
                        .makeText(
                            this@MainActivity,
                            "Error Loading Image",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            })
        }
    }

    @SuppressLint("Recycle")
    private fun setupAnimation() {
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playTogether(login, signup)
        }
    }

    private fun intentToAuth() {
        binding.apply {
            loginButton.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }

            signupButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SignupActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                )
            }
        }
    }


}