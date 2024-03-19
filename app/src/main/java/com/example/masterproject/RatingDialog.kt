package com.example.masterproject

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class RatingDialog(context: Context) : Dialog(context)
{
    private var userrate = 0f

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rating_layout)

        initview()
    }

    private fun initview()
    {
        val btnRateNow = findViewById<AppCompatButton>(R.id.btnRateNow)
        val btnLater = findViewById<AppCompatButton>(R.id.btnLater)
        val rtgbrRating = findViewById<RatingBar>(R.id.rtgbrRating)
        val imgRatingImage = findViewById<ImageView>(R.id.imgRatingImage)

        btnRateNow.setOnClickListener {
            var RatingValue = rtgbrRating.rating
            Toast.makeText(context, "Thanks for rating" +" "+ RatingValue, Toast.LENGTH_SHORT).show()
        }

        btnLater.setOnClickListener {
            dismiss()
        }

        rtgbrRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

            if(rating <= 1)
            {
                imgRatingImage.setImageResource(R.drawable.one_star)
            }

            else if(rating <= 2)
            {
                imgRatingImage.setImageResource(R.drawable.two_star)
            }

            else if(rating <= 3)
            {
                imgRatingImage.setImageResource(R.drawable.three_star)
            }

            else if(rating <= 4)
            {
                imgRatingImage.setImageResource(R.drawable.four_star)
            }

            else if(rating <= 5)
            {
                imgRatingImage.setImageResource(R.drawable.five_star)
            }

            animationImage(imgRatingImage)

            userrate = rating
        }
    }

    private fun animationImage(imgRatingImage: ImageView) {
        // Your animation logic here
        val scaleAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.fillAfter = true
        scaleAnimation.setDuration(200)
        imgRatingImage.startAnimation(scaleAnimation)
    }
}