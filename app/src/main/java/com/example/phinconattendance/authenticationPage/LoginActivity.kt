package com.example.phinconattendance.authenticationPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.phinconattendance.dashboard.DashboardActivity
import com.example.phinconattendance.databinding.ActivityLoginBinding
import com.example.phinconattendance.models.UserCredentialModel
import com.example.phinconattendance.session.UserSession
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("Users")

        setupAction()
        isForgotPassword()
        signUpButton()
    }


    private fun setMyButtonEnable() {
        binding.apply {
            val usernameSet = usernameEditText.text
            val passwordSet = passwordEditText.text
            loginButton.isEnabled =
                usernameSet != null && passwordSet != null && usernameSet.toString()
                    .isNotEmpty() && passwordSet.toString().isNotEmpty()
        }
    }

    private fun setupAction(){
        binding.apply {
            usernameEditText.addTextChangedListener {
                setMyButtonEnable()
            }

            passwordEditText.addTextChangedListener {
                setMyButtonEnable()
            }

            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                showLoading(true)
                database.orderByChild("username").equalTo(username)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (user in snapshot.children) {
                                // do something with the individual "issues"
                                val userCred = user.getValue(UserCredentialModel::class.java)

                                val userSession = UserSession(this@LoginActivity)
                                userCred?.username.let { it1 ->
                                    userSession.saveUsername(
                                        it1.toString()
                                    )
                                }
                                Log.d(
                                    "LoginActivity",
                                    "ID : Username : ${userSession.passUsername()}"
                                )

                                if (userCred?.password.equals(password)
                                ) {
                                    showLoading(false)
                                    Log.d("username", "hasil username : ${userCred?.username}")
                                    Log.d("password", "hasil password : ${userCred?.password}")

                                    AlertDialog.Builder(this@LoginActivity).apply {
                                        setTitle("Yeah!")
                                        setMessage("You are now logging in. Let's connect!")
                                        setPositiveButton("Continue") { _, _ ->
                                            val intent = Intent(context, DashboardActivity::class.java)
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
                                    showLoading(false)
                                    Toast
                                        .makeText(
                                            this@LoginActivity,
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
                                    this@LoginActivity,
                                    "Username/Password is wrong",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }

                    })
            }
        }
    }

    private fun isForgotPassword() {
        binding.apply {
            forgotPasswordButton.setOnClickListener {
                val toSignUp = Intent(this@LoginActivity, ForgotPassActivity::class.java)
                startActivity(toSignUp)
            }
        }
    }


    private fun signUpButton() {
        binding.apply {
            signUpButton.setOnClickListener {
                val toSignUp = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(toSignUp)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
    }

}