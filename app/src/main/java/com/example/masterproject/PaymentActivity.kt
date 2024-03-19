package com.example.masterproject

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import org.json.JSONObject
import com.example.masterproject.databinding.ActivityPaymentBinding
import com.razorpay.PaymentResultWithDataListener

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {

    lateinit var binding: ActivityPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()
    }

    private fun initview() {
        Checkout.preload(applicationContext)
        val co = Checkout()

        co.setKeyID("rzp_test_lGpOjtlY1MPnJA")
//        binding.btnPay.setOnClickListener {
        initpayment()
        finish()
//        }
    }

    private fun initpayment() {
//        checkout.setKeyID("rzp_test_YGcFmNJO6acoGz")
        val checkout = Checkout()
//
        checkout.setImage(R.drawable.logo)


        val activity: Activity = this
        val co = Checkout()

        var username = intent.getStringExtra("username")
        var email = intent.getStringExtra("email")
        var mobile = intent.getStringExtra("mobile")

        try {
            val options = JSONObject()
            options.put("name", username)
            options.put("description", "Reference No. #123456")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("order_id", "order_DBJOWzybf0sJbb") //from response of step 3.
            options.put("theme.color", "#3F51B5")
            options.put("currency", "INR")
            options.put("amount", "50000") //pass amount in currency subunits
            options.put("prefill.email", email)
            options.put("prefill.contact", mobile)
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            co.open(activity, options)
        } catch (e: Exception) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e)
        }
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error : ${p1}", Toast.LENGTH_SHORT).show()
        Log.e("TAG", "onPaymentError: "+p0+p1+p2)
    }
}