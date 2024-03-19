package com.example.masterproject

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.masterproject.databinding.ActivityBooknowBinding
import java.util.Calendar

class BooknowActivity : AppCompatActivity() {

    lateinit var binding: ActivityBooknowBinding

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityBooknowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()
    }

    @SuppressLint("SetTextI18n")
    private fun initview()
    {
        sharedPreferences = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

        // Retrieve data from SharedPreferences
        val username = sharedPreferences.getString("username", "")
        val email = sharedPreferences.getString("email", "")
        val address = sharedPreferences.getString("address", "")
        val mobile = sharedPreferences.getString("mobile", "")
//        Log.e("TAG", "mobile: "+mobile)
        val usertype = sharedPreferences.getInt("usertype", 0)
//        val imageDownloadUrl = sharedPreferences.getString("imageDownloadUrl", "")

        var name = intent.getStringExtra("name")
//        var addres = intent.getStringExtra("address")

        var profileList = Profiles()

        binding.edtfrom.setText(address)
        binding.edtTo.setText(name)
        binding.edtmobile.setText(mobile)
        // Display retrieved data in appropriate views
        binding.edtUsername.setText(username)

        binding.edtemail.setText(email)
//        binding.textViewUsertype.text = "Usertype: $usertype"

        binding.btnsubmit.setOnClickListener {

            var date = binding.edtdate.text.toString()
            var person = binding.edtPerson.text.toString()

            if(binding.edtfrom.text!!.isEmpty())
            {
                binding.edtfrom.error = "Please enter your location"
                addontextchangelistner()
            }
            else if(binding.edtTo.text!!.isEmpty())
            {
                binding.edtTo.error = "Please enter your going location"
                addontextchangelistner()
            }
            else if(binding.edtUsername.text!!.isEmpty())
            {
                binding.edtUsername.error = "Please enter your Username"
                addontextchangelistner()
            }
            else if(binding.edtemail.text!!.isEmpty())
            {
                binding.edtemail.error = "Please enter your email"
                addontextchangelistner()
            }
            else if(binding.edtmobile.text!!.isEmpty())
            {
                binding.edtmobile.error = "Please enter your mobile number"
                addontextchangelistner()
            }
            else if(binding.edtdate.text!!.isEmpty())
            {
                binding.edtdate.error = "Please enter your date of going"
                addontextchangelistner()
            }
            else if(binding.edtPerson.text!!.isEmpty())
            {
                binding.edtPerson.error = "please enter total person"
                addontextchangelistner()
            }
            else
            {

                var i = Intent(this,PaymentActivity::class.java)
//              i.putExtra("address",address)
//              i.putExtra("name",name)
                i.putExtra("username",username)
                i.putExtra("email",email)
                i.putExtra("mobile",mobile)
//              i.putExtra("date",date)
//              i.putExtra("person",person)
                startActivity(i)
            }
        }
            binding.edtdate.setOnClickListener {
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    binding.edtdate.setText(dat)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

    }

    fun addontextchangelistner() {
        binding.edtfrom.addTextChangedListener {
            binding.edtfrom.error = null
        }

        binding.edtTo.addTextChangedListener {
            binding.edtTo.error = null
        }

        binding.edtUsername.addTextChangedListener {
            binding.edtUsername.error = null
        }

        binding.edtemail.addTextChangedListener {
            binding.edtemail.error = null
        }

        binding.edtmobile.addTextChangedListener {
            binding.edtmobile.error = null
        }

        binding.edtdate.addTextChangedListener {
            binding.edtdate.error = null
        }

        binding.edtPerson.addTextChangedListener {
            binding.edtPerson.error = null
        }
    }
}