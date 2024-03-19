package com.example.masterproject

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.masterproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity()
{
    lateinit var auth: FirebaseAuth
    lateinit var reference: DatabaseReference
    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var firebaseDatabase: FirebaseDatabase

    val firebaseAuth = FirebaseAuth.getInstance()

    companion object {
        lateinit var user: Profiles
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences=getSharedPreferences("MySharedPreferences", MODE_PRIVATE)

        initview()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initview()
    {
        // Initialize Firebase Auth
        auth = Firebase.auth

        auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance()

        reference = FirebaseDatabase.getInstance().reference

        binding.txtSignup.setOnClickListener {
            var i = Intent(this, Signup_Activity::class.java)
            startActivity(i)
            finish()
        }

        if (sharedPreferences.getBoolean("isLogin",false) == true)
        {
            var i = Intent(this, DashboardActivity::class.java)
            startActivity(i)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            var username = binding.edtUsername.text.toString()
            var email = binding.edtemail.text.toString()
            var password = binding.edtPasswordToggle.text.toString()

            if(username.isEmpty())
            {
                Toast.makeText(this, "Please Enter username", Toast.LENGTH_SHORT).show()
            }
            else if (email.isEmpty())
            {
                Toast.makeText(this, "Please Enter E-mail", Toast.LENGTH_SHORT).show()
            }
            else if (password.isEmpty())
            {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
            }
            else if (username.isEmpty() && email.isEmpty() && password.isEmpty())
            {
                Toast.makeText(this, "Please fill all the field", Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth = FirebaseAuth.getInstance()

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()

                        firebaseDatabase.reference.root.child("UsertypeTb").child(username).addValueEventListener(object : ValueEventListener{

                            override fun onDataChange(snapshot: DataSnapshot) {

                                user = snapshot.getValue(Profiles::class.java)!!

                                var sharedPreferences: SharedPreferences.Editor = sharedPreferences.edit()
                                sharedPreferences.putBoolean("isLogin", true)
                                sharedPreferences.putString("email",user.email)
                                sharedPreferences.putString("username",user.Username)
                                sharedPreferences.putString("address",user.address)
                                sharedPreferences.putString("imageDownloadUrl",user.profile)
                                sharedPreferences.putInt("usertype",user.UserAdmin)
                                sharedPreferences.apply()

                                var i = Intent(this@LoginActivity,DashboardActivity::class.java)
                                startActivity(i)
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("TAG", "onCancelled: "+error.message)
                            }
                        })
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}