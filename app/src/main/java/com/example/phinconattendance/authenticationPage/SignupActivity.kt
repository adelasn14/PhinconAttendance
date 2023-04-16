package com.example.phinconattendance.authenticationPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.phinconattendance.databinding.ActivitySignupBinding
import com.example.phinconattendance.models.UserCredentialModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.getReference("Users")

        setupAction()
        toLoginButton()
    }


    private fun setMyButtonEnable() {
        binding.apply {
            val usernameSet = usernameEditText.text
            val passwordSet = passwordEditText.text
            val fullnameSet = fullnameEditText.text
            val repeatPassSet = repeatPassEditText.text
            loginButton.isEnabled =
                usernameSet != null && usernameSet.toString()
                    .isNotEmpty() && passwordSet.toString().isNotEmpty() && passwordSet != null &&
                        fullnameSet != null && fullnameSet.toString()
                    .isNotEmpty() && repeatPassSet != null && repeatPassSet.toString().isNotEmpty()
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

            fullnameEditText.addTextChangedListener {
                setMyButtonEnable()
            }

            repeatPassEditText.addTextChangedListener {
                setMyButtonEnable()
            }

            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                val fullname = fullnameEditText.text.toString()

                showLoading(true)

                val userCred = UserCredentialModel(fullname,username,password)
                database.child(fullname).setValue(userCred).addOnSuccessListener {
                    showLoading(false)
                    Log.d("username", "hasil username, password, fullname : ${userCred.username}, ${userCred.password}, ${userCred.name}")

                    AlertDialog.Builder(this@SignupActivity).apply {
                        setTitle("Yeah!")
                        setMessage("Account successfully registered. Let's log you in!")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(context, LoginActivity::class.java)
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
                        Toast.makeText(this@SignupActivity, "Failed to register", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun toLoginButton() {
        binding.apply {
            toLoginButton.setOnClickListener {
                val toLogin = Intent(this@SignupActivity, LoginActivity::class.java)
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