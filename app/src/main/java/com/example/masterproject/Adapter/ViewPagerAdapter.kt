package com.example.masterproject.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.masterproject.R
import java.util.ArrayList

class ViewPagerAdapter(var context: Context, var ListViewPager: ArrayList<String>?) : PagerAdapter(){
    override fun getCount(): Int
    {
        return ListViewPager!!.size
    }

    override fun isViewFromObject(view: View, objects: Any): Boolean
    {
        return view == objects
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        var view=LayoutInflater.from(context).inflate(R.layout.itemview,container,false)

        var imageView : ImageView = view.findViewById(R.id.imageSwiper)

        Glide.with(context).load(ListViewPager?.get(position)).into(imageView) // Load image from the provided URL

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, objects: Any)
    {
//        container.removeView(objects as View?)
        val vp = container as ViewPager
        val view = objects as View
        vp.removeView(view)
    }
}