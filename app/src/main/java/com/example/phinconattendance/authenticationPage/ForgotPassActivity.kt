package com.example.phinconattendance.authenticationPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.phinconattendance.databinding.ActivityForgotPassBinding
import com.example.phinconattendance.models.UserCredentialModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPassBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("Users")


        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

        setupAction()
        toLoginButton()
    }


    private fun setMyButtonEnable() {
        binding.apply {
            val ktpSet = KTPEditText.text
            val passwordSet = passwordEditText.text
            resetButton.isEnabled =
                ktpSet != null && passwordSet != null && ktpSet.toString()
                    .isNotEmpty() && passwordSet.toString().isNotEmpty()
        }
    }

    private fun setupAction(){
        binding.apply {
            KTPEditText.addTextChangedListener {
                setMyButtonEnable()
            }

            passwordEditText.addTextChangedListener {
                setMyButtonEnable()
            }

            resetButton.setOnClickListener {
                val ktp = KTPEditText.text.toString()
                val password = passwordEditText.text.toString()

                showLoading(true)
                database.orderByChild("nik").equalTo(ktp)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (user in snapshot.children) {
                                // do something with the individual "issues"
                                val userCred = user.getValue(UserCredentialModel::class.java)
                                if (userCred?.password.equals(password)
                                ) {
                                    showLoading(false)
                                    database.child(userCred?.name.toString()).removeValue()
                                    Log.d("username", "hasil ktp : ${userCred?.nik}")
                                    Log.d("password", "hasil password : ${userCred?.password}")

                                    AlertDialog.Builder(this@ForgotPassActivity).apply {
                                        setTitle("Yeah!")
                                        setMessage("Account successfully reset. Let's sign you up again!")
                                        setPositiveButton("Continue") { _, _ ->
                                            val intent = Intent(context, SignupActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                            }
                                else {
                                    Toast
                                        .makeText(
                                            this@ForgotPassActivity,
                                            "Username/Password is wrong",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast
                                .makeText(
                                    this@ForgotPassActivity,
                                    "Username/Password is wrong",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }

                    })
            }
        }
    }

    private fun toLoginButton() {
        binding.apply {
            toLoginButton.setOnClickListener {
                val toLogin = Intent(this@ForgotPassActivity, LoginActivity::class.java)
                startActivity(toLogin)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
    }

}