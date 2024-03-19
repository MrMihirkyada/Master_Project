package com.example.masterproject.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.masterproject.PackageInfoActivity
import com.example.masterproject.Packages
import com.example.masterproject.Profiles
import com.example.masterproject.R
import com.example.masterproject.databinding.HomeItemBinding
import com.google.firebase.database.ValueEventListener

class PackageAdapter(var context: ValueEventListener, var packagesList: ArrayList<Packages>,var profilesList : ArrayList<Profiles>) : RecyclerView.Adapter<PackageAdapter.MyviewHolder>()
{
    class MyviewHolder(binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        var lnrdetails = binding.lnrdetails
        var imgfirstimage = binding.imgfirstimage
        var txtName = binding.txtName
        var txtPrice = binding.txtPrice
        var txtDays = binding.txtDays
        var txtNotes = binding.txtNotes
        var imgPackageInfo = binding.imgPackageInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder
    {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent,false)
        var holder = MyviewHolder(HomeItemBinding.bind(v))
        return holder
    }

    override fun getItemCount(): Int
    {
        return packagesList.size
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int)
    {
        holder.txtName.text = packagesList[position].name
        Glide.with(holder.itemView.context).load(packagesList[position].imageUrl[0]).into(holder.imgfirstimage)
        holder.txtPrice.text = "₹ "+packagesList[position].price.toString()+"/-"
        holder.txtDays.text = packagesList[position].days.toString()
        holder.txtNotes.text = packagesList[position].notes

        holder.imgPackageInfo.setOnClickListener {
            var i = Intent(holder.itemView.context,PackageInfoActivity::class.java)
            i.putExtra("name",packagesList[position].name)
            i.putExtra("mobile",packagesList[position].mobile)
//            i.putExtra("address",profilesList[position].address)
            i.putExtra("allImageList",packagesList[position].imageUrl)
            i.putExtra("Price","₹ "+packagesList[position].price+"/-")
            i.putExtra("day",packagesList[position].days)
            i.putExtra("Notes",packagesList[position].notes)
            Log.e("TAG", "onBindViewHolder: "+packagesList[position].imageUrl)
//            ViewPagerAdapter(contexts,packagesList[position].imageUrl)
            holder.itemView.context.startActivity(i)
        }

        holder.lnrdetails.setOnClickListener{
            var i = Intent(holder.itemView.context,PackageInfoActivity::class.java)
            i.putExtra("name",packagesList[position].name)
            i.putExtra("mobile",packagesList[position].mobile)
//            i.putExtra("address",profilesList[position].address)
            i.putExtra("allImageList",packagesList[position].imageUrl)
            i.putExtra("Price","₹ "+packagesList[position].price+"/-")
            i.putExtra("day",packagesList[position].days)
            i.putExtra("Notes",packagesList[position].notes)
            Log.e("TAG", "onBindViewHolder: "+packagesList[position].imageUrl)
            holder.itemView.context.startActivity(i)
        }
    }
}