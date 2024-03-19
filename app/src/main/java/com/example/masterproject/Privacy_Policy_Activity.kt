package com.example.masterproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.masterproject.databinding.ActivityPrivacyPolicyBinding

class Privacy_Policy_Activity : AppCompatActivity() {

    lateinit var web: WebView

    lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()
    }

    private fun initview() {
        web = findViewById<WebView>(R.id.webView)
        web.loadUrl("https://www.termsfeed.com/live/27c7c9b3-d1cf-4e6b-8a04-7b4c76ece66a")
    }
}