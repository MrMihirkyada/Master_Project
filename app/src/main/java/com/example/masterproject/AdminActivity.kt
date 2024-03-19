package com.example.masterproject

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.masterproject.databinding.ActivityAdminBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AdminActivity : AppCompatActivity() {
    lateinit var reference: DatabaseReference

    lateinit var storage: FirebaseStorage

    lateinit var storageReference: StorageReference

    private val PICK_IMAGES_REQUEST = 1

    lateinit var textView: TextView

    private val ImageList = ArrayList<Uri>()

    private var uploads = 0
    private val progressDialog: ProgressDialog? = null

    lateinit var databaseReference: DatabaseReference

    var index = 0

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityAdminBinding
        var allImageList = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
            initview()
    }

    private fun initview()
    {
        reference = FirebaseDatabase.getInstance().reference

        storage = FirebaseStorage.getInstance()
        //        storageReference = storage.reference
        storageReference = FirebaseStorage.getInstance().reference

        textView = findViewById(R.id.text)

        databaseReference = FirebaseDatabase.getInstance().getReference().child("packageTb")
        binding.imgbtnaddimage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGES_REQUEST)
        }
        binding.txtUploadImages.setOnClickListener {
            if (ImageList.isNotEmpty())
            {
                    uploadImagesToFirebase()
            }
            else
            {
                Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (ImageList.isNotEmpty()) {
                // Do not upload images again here, as they have already been uploaded
                storeDataInDatabase()
            } else {
                Toast.makeText(this, "Please upload images first", Toast.LENGTH_SHORT).show()
            }

//                val key = reference.child("packageTb").push().key ?: ""
//
//                var name = binding.edtName.text.toString()
//                var price = binding.edtPrice.text.toString().toInt()
//                var days = binding.edtDays.text.toString().toInt()
//                var notes = binding.edtNotes.text.toString()
//
//
//                var packages = Packages(key,ImageList, name, price, days, notes)
//
//                Log.e(
//                    "TAG",
//                    "images: " + key + "  " + name + "  " + price + "  " + days + "  " + notes + "  "
//                )
//                reference.child("packageTb").child(key).setValue(packages).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT)
//                            .show()
//                        var i = Intent(this, HomeFragment::class.java)
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        finish()
//                        //                    startActivity(i)
//                    }
//                }.addOnFailureListener {
//                    Log.e("TAG", "initView: " + it.message)
//                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
//                }
//            }
            }
        }

        private fun uploadImagesToFirebase()
        {
            val imageFolder = FirebaseStorage.getInstance().reference.child("ImageFolder")
            val imageUrls = ArrayList<String>()

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading images...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            var successfulUploads = 0

            ImageList.forEachIndexed { index, image ->
                val imageName = imageFolder.child("image_${index + 1}.jpg")
                imageName.putFile(image)
                    .addOnSuccessListener { taskSnapshot ->
                        imageName.downloadUrl.addOnSuccessListener { uri ->
                            val url = uri.toString()
                            imageUrls.add(url)
                            successfulUploads++

                            if (successfulUploads == ImageList.size)
                                {
                                // All images uploaded successfully
                                allImageList = imageUrls
                                progressDialog.dismiss()
                                // Show toast message
                                Toast.makeText(this, "All images uploaded successfully", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun storeDataInDatabase() {
            val key = reference.child("packageTb").push().key ?: ""
            val name = binding.edtPlaceName.text.toString()
//            val price = binding.edtPrice.text.toString().toInt()
//            val days = binding.edtDays.text.toString().toInt()
            val notes = binding.edtNotes.text.toString()

            val priceText = binding.edtPrice.text.toString()
            val price = if (priceText.isNotEmpty()) {
                priceText.toInt()
            } else {
                // Handle the case when the input is empty
                0 // Default value or any other appropriate value
            }

            var mobile = binding.edtMobile.text.toString()
            val daysText = binding.edtDays.text.toString()
            val days = if (daysText.isNotEmpty()) {
                daysText.toInt()
            } else {
                // Handle the case when the input is empty
                0 // Default value or any other appropriate value
            }

            val packages = Packages(key, allImageList, name,mobile, price, days, notes)

            reference.child("packageTb").child(key).setValue(packages)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show()
                        // Clear input fields
                        val place = binding.edtPlaceName.text?.clear()
                        var prices = binding.edtPrice.text?.clear()
                        var day = binding.edtDays.text?.clear()
                        var mobile = binding.edtMobile.text?.clear()
                        var note = binding.edtNotes.text?.clear()
                        ImageList.clear()

                        val placeName = binding.edtPlaceName.text.toString()
                        val additionalNotes = binding.edtNotes.text.toString()

                    Log.e("TAG", "storeDataInDatabase: "+place+notes)
                    val customNotification = CustomNotification(this,name,notes)
                    customNotification.show()
                } else {
                   Toast.makeText(this, "Failed to insert data.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        ImageList.add(imageUri)
                    }
                    textView.visibility = View.VISIBLE
                    textView.text = "You Have Selected ${ImageList.size} Pictures"
                }
            }
        }
    }

    class Packages
    {
        var id = ""
        var imageUrl : ArrayList<String> = ArrayList()
        var name = ""
        var mobile = ""
        var price = 0
        var days = 0
        var notes = ""

        constructor(id: String,imageUrl : ArrayList<String> = ArrayList(),name : String,mobile : String, price : Int, days : Int, notes : String)
        {
            this.id = id
            this.imageUrl = imageUrl
            this.name = name
            this.mobile = mobile
            this.price = price
            this.days = days
            this.notes = notes
        }

        constructor()
        {

        }
    }