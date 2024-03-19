package com.example.masterproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.masterproject.Fragment.HomeFragment
import com.example.masterproject.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class DashboardActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    companion object
    {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityDashboardBinding

//        private const val LOGGED_OUT_KEY = "logged_out"

        @SuppressLint("StaticFieldLeak")
        lateinit var contexts: Context // Add this line to store the context

        // Modify this function to accept a context parameter
        fun initialize(contexts: Context)
        {
            this.contexts = contexts
        }

        val sharedPref by lazy {
            contexts.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        }

        val UserAdmin: Int get() = (sharedPref.getInt("UserAdmin", 0) ?: "") as Int
    }

    lateinit var reference: DatabaseReference

    lateinit var lnrHotel: LinearLayout
    lateinit var versionTV: TextView
    lateinit var downloadUrl: Uri
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var sharedPreferences: SharedPreferences

    lateinit var mDialogButton: LinearLayout
    lateinit var okay_text: TextView
    lateinit var cancel_text: TextView

    @RequiresApi(api = Build.VERSION_CODES.P)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Companion.initialize(this)
//      window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)

        sharedPreferences=getSharedPreferences("MySharedPreferences", MODE_PRIVATE)

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,1)
        checkPermission(Manifest.permission.CALL_PHONE,2)
        checkPermission(Manifest.permission.SEND_SMS,3)

        initview()
    }

    private fun checkPermission(permission: String, requestCode: Int)
    {
        if (ContextCompat.checkSelfPermission(this@DashboardActivity, permission) == PackageManager.PERMISSION_DENIED)
        {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@DashboardActivity, arrayOf(permission), requestCode)
        }
//        else
//        {
//            Toast.makeText(this@DashboardActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this@DashboardActivity, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            }
//            else
//            {
//                Toast.makeText(this@DashboardActivity, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
//            }
        }
        if(requestCode == 2)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this@DashboardActivity, "Call Log Permission Granted", Toast.LENGTH_SHORT).show()
            }
//            else
//            {
//                Toast.makeText(this@DashboardActivity, "Call Log Permission Denied", Toast.LENGTH_SHORT).show()
//            }
        }
        if(requestCode == 3)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this@DashboardActivity, "SMS Permission Granted", Toast.LENGTH_SHORT).show()
            }
//            else
//            {
//                Toast.makeText(this@DashboardActivity, "SMS Permission Denied", Toast.LENGTH_SHORT).show()
//            }
        }
    }
    override fun onResume() {
        super.onResume()
        getFirebaseProfile()
    }

    @SuppressLint("SetTextI18n", "ApplySharedPref", "SuspiciousIndentation")
    private fun initview() {

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.versionCode

        // Display the version name and version code in TextViews
        binding.txtVersionNumber.text = "Version Name: $versionName"
        binding.txtVersionCode.text = "Version Code: $versionCode"

        binding.imgmenu.setOnClickListener {
            binding.drawer.openDrawer(binding.navigationview)
            Log.e("TAG", "nav: " + binding.navigationview)
        }

        binding.lnrProfile.setOnClickListener {
            binding.drawer.closeDrawer(binding.navigationview)
        }

        binding.lnrprivacypolicy.setOnClickListener {
            var i = Intent(this@DashboardActivity, Privacy_Policy_Activity::class.java)
            startActivity(i)
        }

        binding.linsharewithfriend.setOnClickListener {
//            val shareContent = ""
//            shareApp(this, shareContent)
        }

        binding.linrateapp.setOnClickListener {
            var ratingusdialog = RatingDialog(this@DashboardActivity)
            ratingusdialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
            ratingusdialog.setCancelable(false)
            ratingusdialog.show()
        }

        binding.lnrLogout.setOnClickListener {
            var sharedPreferences: SharedPreferences.Editor = sharedPreferences.edit()
            sharedPreferences.remove("isLogin")
            sharedPreferences.commit()
            logout()
//          auth.signOut()
//          var i = Intent(this, LoginActivity::class.java)
//          Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show()
//          startActivity(i)
//          finish()
//          var i = Intent(this, LoginActivity::class.java )
//          startActivity(i)
//          finish()
        }

//        binding.btnNotification.setOnClickListener{
//            var i = Intent(this@DashboardActivity,NotificationSendingActivity::class.java)
//            startActivity(i)
//            val customNotification = CustomNotification(this)
//            customNotification.show()
//        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.LoutFrame, HomeFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        binding.txthome.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.LoutFrame, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val isNewUser = sharedPref.getBoolean("isLogin", false)

            getFirebaseProfile()
    }

    private fun getFirebaseProfile() {
        firebaseDatabase = FirebaseDatabase.getInstance()

        auth = FirebaseAuth.getInstance()

        auth.currentUser?.let {
            firebaseDatabase.reference.root.child("UsertypeTb").child("username").addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {

                    sharedPreferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE)

                    val user = snapshot.getValue(Profile::class.java)
                    // Replace "your_key" with the actual key you used to store the data
                    val usernames = sharedPreferences.getString("username","")
                    val emails = sharedPreferences.getString("email","")
                    val image = sharedPreferences.getString("imageDownloadUrl","")

                    var usertype = sharedPreferences.getInt("usertype",0)

                    Log.e("TAG", "onDataChangess: "+usertype)

                    var sharedPreferences: SharedPreferences.Editor = sharedPreferences.edit()
                    val userTypeText = when (usertype) {
                        0 -> "User"
                        else -> "Admin"
                    }
                    sharedPreferences.putInt("usertype",usertype)

//                  Log.e("TAG", "onDataChange: ===> $username, $email, $image" )

                    binding.txtUsername.text = "username: $usernames"
                    binding.txtemail.text = "email: $emails"
                    binding.txtusertype.text = "usertype: $userTypeText"
                    Glide.with(this@DashboardActivity).load(image).into(binding.civProfileImages)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DashboardActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun logout()
    {
        mDialogButton = findViewById<LinearLayout>(R.id.lnrLogout)
        val dialog = Dialog(this@DashboardActivity)

        mDialogButton.setOnClickListener{

            dialog.setContentView(R.layout.custom_dialog_logout)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(false)
            dialog.window?.attributes?.windowAnimations = R.style.animation

            okay_text = dialog.findViewById(R.id.okay_text)
            cancel_text = dialog.findViewById(R.id.cancel_text)

            okay_text.setOnClickListener {
                auth.signOut()
                var i = Intent(this, LoginActivity::class.java)
                Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show()
                startActivity(i)
                finish()
                dialog.dismiss()
//                Toast.makeText(this, "okay clicked", Toast.LENGTH_SHORT).show()
            }

            cancel_text.setOnClickListener{
                dialog.dismiss();
                Toast.makeText(this, "Cancel clicked", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }

    fun shareApp(context: Context, shareContent: String)
    {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareContent)
        }

        val chooser = Intent.createChooser(shareIntent, "Share via")
        if (shareIntent.resolveActivity(context.packageManager) != null)
        {
            context.startActivity(chooser)
        }
    }

//    private fun snackbar() {
//        lnrHotel = findViewById(R.id.txthome)
//
////        binding.txtusertype.text = usertype.toString()
//
//        lnrHotel.setOnClickListener(object : View.OnClickListener {
//            @SuppressLint("MissingInflatedId", "InflateParams")
//            override fun onClick(v: View?) {
//
//                val connectivityManager = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//                val networkInfo = connectivityManager.activeNetworkInfo
//
//                if (networkInfo == null || !networkInfo.isConnected || !networkInfo.isAvailable) {
//                    val snackbar = v?.let { Snackbar.make(it, "", Snackbar.LENGTH_LONG) }
//
//                    val customSnackView: View = layoutInflater.inflate(R.layout.no_connection_snackbar_layout, null)
//
////                    binding.progressbar.visibility = View.VISIBLE
//                    snackbar?.view?.setBackgroundColor(Color.TRANSPARENT)
//
//                    val snackbarLayout = snackbar?.view as Snackbar.SnackbarLayout
//
//                    snackbarLayout.setPadding(0, 0, 0, 0)
//
//                    snackbarLayout.addView(customSnackView, 0)
//                    snackbar.show()
//                } else {
//                    val snackbar = v?.let { Snackbar.make(it, "", Snackbar.LENGTH_LONG) }
//
//                    val customSnackView: View =  layoutInflater.inflate(R.layout.connaction_success_layout, null)
//
////                    binding.progressbar.visibility = View.GONE
//                    snackbar?.view?.setBackgroundColor(Color.TRANSPARENT)
//
//                    val snackbarLayout = snackbar?.view as Snackbar.SnackbarLayout
//
//                    snackbarLayout.setPadding(0, 0, 0, 0)
//
//                    snackbarLayout.addView(customSnackView, 0)
//                    snackbar.show()
//                }
//            }
//        })
//
////        if(usertype == 0)
////        {
////            var i = Intent(this,UserActivity::class.java)
////            startActivity(i)
////        }
////        else
////        {
////            var i = Intent(this,AdminActivity::class.java)
////            startActivity(i)
//
//
////        }
//
//    }
}