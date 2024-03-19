package com.example.masterproject

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.masterproject.Adapter.ViewPagerAdapter
import com.example.masterproject.databinding.ActivityPackageInfoBinding

class PackageInfoActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var binding: ActivityPackageInfoBinding

    lateinit var imageList: MutableList<String>

    lateinit var Adapter : ViewPagerAdapter
//    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtDialogButton : TextView
    lateinit var mobiles : String
    lateinit var txtYes: TextView
    lateinit var txtMobiles: TextView
    lateinit var txtNo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityPackageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()
    }


    @SuppressLint("SetTextI18n")
    private fun initview() {

//        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        viewPager = findViewById(R.id.vipImageSlider)

        imageList = mutableListOf()

//        mobile = sharedPreferences.getString("mobile", "").toString()


        var name = intent.extras?.getString("name")
        mobiles = intent.extras?.getString("mobile").toString()
        var address = intent.extras?.getString("address")
        var allImageLists = intent.extras?.getStringArrayList("allImageList")
        var day = intent.extras?.getInt("day")
        var Price = intent.extras?.getString("Price")
        var notes = intent.extras?.getString("Notes")

        binding.txtName.text = name
        binding.txtNotes.text = notes
        binding.txtMobile.text = mobiles
        binding.txtPrices.text = Price
        binding.txtDays.text = "Day :- $day"

        binding.txtMobile.setOnClickListener {
            phoneCall()
        }

        binding.btnbooknow.setOnClickListener{
            var i = Intent(this,BooknowActivity::class.java)
            i.putExtra("name",name)
            startActivity(i)
        }

        Log.e("TAG", "imageUrls: "+allImageLists)
        Adapter = ViewPagerAdapter(this, allImageLists)
        viewPager.adapter = Adapter
    }
    private fun phoneCall() {
        txtDialogButton = findViewById<TextView>(R.id.txtMobile)
        val dialog = Dialog(this@PackageInfoActivity)

        txtDialogButton.setOnClickListener {

            dialog.setContentView(R.layout.phone_call_layout)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(false)
            dialog.window?.attributes?.windowAnimations = R.style.animation

            txtYes = dialog.findViewById(R.id.txtbtnYes)
            txtNo = dialog.findViewById(R.id.txtbtnNo)
            txtMobiles = dialog.findViewById(R.id.txtMobiles)

            txtMobiles.text = mobiles
            txtYes.setOnClickListener {
                val number = mobiles
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$number")
                startActivity(callIntent)
            }

            txtNo.setOnClickListener {
                dialog.dismiss()
//                Toast.makeText(this, "No clicked", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }
    }
}