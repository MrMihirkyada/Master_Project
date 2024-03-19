package com.example.masterproject

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.masterproject.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.UUID

class Signup_Activity : AppCompatActivity()
{
    lateinit var binding: ActivitySignupBinding

    private lateinit var auth: FirebaseAuth

    lateinit var reference: DatabaseReference

    lateinit var radioGroup: RadioGroup

    var downloadUrl: Uri? = null

    var PICK_IMAGE_REQUEST = 22

    lateinit var filePath: Uri

    //    companion object {
    var selectedRadioButtonText = ""

    var selectedRadioButtonId = -1

    var UserAdmin = selectedRadioButtonId
//    }

    lateinit var profileImageBitmap: Bitmap

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    //    companion object {
    lateinit var sharedPreferences: SharedPreferences
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()
    }

    private fun initview() {
        auth = Firebase.auth

        val actionBar: ActionBar? = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#0F9D58"))
        actionBar?.setBackgroundDrawable(colorDrawable)

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        sharedPreferences = getSharedPreferences("my_shared_pref_file", Context.MODE_PRIVATE)

        binding.civProfileImage.setOnClickListener {
//            selectImage.launch("image/*")
            SelectImage()
        }

        binding.txtUploadImage.setOnClickListener {
            uploadImage()
        }

        reference = FirebaseDatabase.getInstance().reference

        radioGroup = findViewById(R.id.rggroup)

        if (FirebaseAuth.getInstance().currentUser != null) {
            // If the user is already authenticated, navigate to the dashboard
            var i = Intent(this, DashboardActivity::class.java)
            startActivity(i)
            finish()
        }

        if (sharedPreferences.getBoolean("isLogin", false) == true) {
            var i = Intent(this, DashboardActivity::class.java)
            startActivity(i)
            finish()
        }

        binding.btnSignup.setOnClickListener {
            selectedRadioButtonId = radioGroup.checkedRadioButtonId

            var profile = ""
            if (downloadUrl != null) {
                profile = downloadUrl.toString()
            }

            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            selectedRadioButtonText = selectedRadioButton.text.toString()
            Log.e("TAG", "UserAdmin ==>" + selectedRadioButtonText)

            when (val index: Int = radioGroup.indexOfChild(selectedRadioButton)) {
                0 -> {
                    selectedRadioButtonId = 0
                }

                1 -> {
                    selectedRadioButtonId = 1
                }
            }

            Log.e("TAG", "selectedRadioButtonId: " + selectedRadioButtonId)

            val email = binding.edtemail.text.toString()
            val password = binding.edtPasswordToggle.text.toString()
            val Username = binding.edtUsername.text.toString()
            val address = binding.edtaddress.text.toString()
            val mobile = binding.edtmobile.text.toString()
            UserAdmin = selectedRadioButtonId

            if (TextUtils.isEmpty(binding.edtemail.text.toString())) {
                binding.edtemail.error = "Please Enter Email Address"
                Log.e("TAG", "initview: "+email)
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                binding.edtemail.error = "Please Enter valid Email Address"
                Log.e("TAG", "initview: "+email)
            }
            else if (TextUtils.isEmpty(binding.edtPasswordToggle.text.toString()))
            {
                binding.edtPasswordToggle.error = "Please Enter Password"
                Log.e("TAG", "initview: "+password)
            }
            else if (TextUtils.isEmpty(binding.edtUsername.text.toString()))
            {
                binding.edtUsername.error = "Please Enter Username"
                Log.e("TAG", "initview: "+Username)
            }
            else if (!validateUsername(Username))
            {
                binding.edtUsername.error = "Please enter a valid Username"
                return@setOnClickListener
            }
            else if (TextUtils.isEmpty(binding.edtaddress.text.toString()))
            {
                binding.edtaddress.error = "Please Enter Address"
                Log.e("TAG", "initview: "+address)
            }
            else if (TextUtils.isEmpty(binding.edtmobile.text.toString()))
            {
                binding.edtmobile.error = "Please Enter Mobile Number"
                Log.e("TAG", "initview: "+mobile)
            }
            else
            {
                binding.pgbar.visibility = View.VISIBLE

                Log.e("TAG", "Mihir:------- " + email)
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

                    if (it.isSuccessful)
                    {
                        Log.e("TAG", "initview:======= " + auth)
                        var profiles = Profiles(email, email,profile, password, Username, address, mobile, UserAdmin)

//                      signup(email,email, profile, password, Username, address, mobile, UserAdmin)
                        saveUserData(/*profile,*/Username,email,address,selectedRadioButtonId,mobile)

                        reference.root.child("UsertypeTb").child(Username).setValue(profiles).addOnSuccessListener {

                                binding.pgbar.visibility = View.GONE
                                var i = Intent(this, DashboardActivity::class.java)
                                startActivity(i)
                                finish()

                            }.addOnFailureListener {
                                Log.e("TAG", "initview:" + it.message)
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                    //    var i = Intent(this, DashboardActivity::class.java)
                    //    startActivity(i)
                    }
                }.addOnFailureListener {
                    Log.e("TAG", "initview:-----" + it.message)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun SelectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image from here..."),
            PICK_IMAGE_REQUEST
        )
    }

    val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> uri?.let {
            saveImageUri(uri)
            // Optionally, you can load/display the selected image in the ImageView
            binding.civProfileImage.setImageURI(uri)
        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            // checking request code and result code
            // if request code is PICK_IMAGE_REQUEST and
            // resultCode is RESULT_OK
            // then set image in the image view
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
                // Get the Uri of data
                filePath = data.data!!
                try {
                    // Setting image on image view using Bitmap
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    binding.civProfileImage.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        private fun uploadImage()
        {
            if (filePath != null)
            {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())


                ref.putFile(filePath).addOnSuccessListener { // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss()
                    Toast.makeText(this@Signup_Activity, "Image Uploaded!!", Toast.LENGTH_SHORT)
                        .show()

                    ref.downloadUrl.addOnSuccessListener { uri ->

                        downloadUrl = uri
                        Log.e("TAG", "uploadImage: downloadable URL $uri")

                    }
                }.addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast.makeText(this@Signup_Activity, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }.addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }
            }
        }

        private fun saveUserData(username: String, email: String, address: String, UserAdmin: Int, mobile: String)
        {
            val sharedPref = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putString("username", username)
            editor.putString("email", email)
            editor.putString("address",address)
            editor.putInt("usertype", UserAdmin)
            editor.putString("mobile",mobile)
            if (downloadUrl != null) {
                editor.putString("imageDownloadUrl", downloadUrl.toString())
            } else {
                editor.putString("imageDownloadUrl", "")
            }

            editor.putBoolean("isLogin", true)

            editor.apply()

        }

        private fun saveImageUri(uri: Uri) {
            val editor = sharedPreferences.edit()
            editor.putString("imageUri", uri.toString())
            editor.apply()
        }

    fun validateUsername(username: String): Boolean {
        val regex = Regex("^[A-Za-z][A-Za-z0-9_]{3,29}$")
        val isValid = regex.matches(username)
        if (!isValid) {
            Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }
}

class Profiles {
    var id = ""
    var email = ""
    var profile = ""
    var password = ""
    var Username = ""
    var address = ""
    var mobile = ""
    var UserAdmin = 0

    constructor(id: String, email: String, profile: String, password: String, Username: String, address: String, mobile: String, UserAdmin: Int)
    {
        this.id = id
        this.email = email
        this.profile = profile
        this.password = password
        this.Username = Username
        this.address = address
        this.mobile = mobile
        this.UserAdmin = UserAdmin
    }

    constructor() {

    }
}
