package com.example.masterproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.core.content.ContextCompat.startActivity
import com.example.masterproject.databinding.ActivitySplashScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //full screen
//          window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.statusBarColor = this.resources.getColor(R.color.blue)

//          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

//          auth = FirebaseAuth.getInstance()
//
//          // Check if the user is already logged in
//          val currentUser = auth.currentUser
//          if (currentUser != null)
//          {
//              // User is already logged in, open DashboardActivity
//              val intent = Intent(this@SplashScreenActivity, DashboardActivity::class.java)
//              startActivity(intent)
//              finish()
//          }
//          else
//          {
//              // User is not logged in, show login/signup screen
//          }
            initview()
//      }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initview()
    {
        auth = Firebase.auth

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()

        // Check if the user is already logged in
        val currentUser = auth.currentUser

        if (currentUser != null)
        {
            Handler().postDelayed({

                val i = Intent(this@SplashScreenActivity, DashboardActivity::class.java)
                startActivity(i)
                finish()

            }, 2000)
        }
        else
        {
            Handler().postDelayed({

                val i = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }, 2000)
        }
    }

}