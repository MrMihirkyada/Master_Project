package com.example.masterproject.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.masterproject.Adapter.PackageAdapter
import com.example.masterproject.AdminActivity
import com.example.masterproject.DashboardActivity.Companion.UserAdmin
import com.example.masterproject.Packages
import com.example.masterproject.Profiles
import com.example.masterproject.Signup_Activity
import com.example.masterproject.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var reference: DatabaseReference

    lateinit var storage: FirebaseStorage

    lateinit var storageReference: StorageReference

//    var selectedRadioButtonText = ""
    lateinit var sharedPreferences: SharedPreferences

    lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initview()

        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initview() {

        reference = FirebaseDatabase.getInstance().reference

        storage = FirebaseStorage.getInstance()

        storageReference = storage.reference

        binding.pgsbars.visibility = View.VISIBLE


            sharedPreferences = context!!.getSharedPreferences("MySharedPreferences", AppCompatActivity.MODE_PRIVATE)

        var usertype = sharedPreferences.getInt("usertype",0)
        if (usertype != 0)
        {
//                Log.e("TAG", "UserAdmins: "+selectedRadioButtonText)
            Log.e("TAG", "UserAdmins: "+ UserAdmin)
            binding.add.visibility = View.VISIBLE
        }
        else
        {
//            Log.e("TAG", "UserAdmins: "+selectedRadioButtonText)

            Log.e("TAG", "UserAdmins: "+ UserAdmin)
            binding.add.visibility = View.GONE
        }


        binding.add.setOnClickListener {
            var i = Intent(context,AdminActivity::class.java)
            startActivity(i)
        }

        reference.root.child("packageTb").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                var packages : ArrayList<Packages> = ArrayList()
                var Profiles : ArrayList<Profiles> = ArrayList()

                for(data in snapshot.children)
                {
                    var packagesdata = data.getValue(Packages::class.java)

                    Log.e("TAG", "onDataChange: "+packagesdata?.id+packagesdata?.name+packagesdata?.price+packagesdata?.days+packagesdata?.notes)

                    packagesdata?.let { packages.add(it) }
                }

                packages.reverse()

                var adapter = PackageAdapter(this,packages,Profiles)

                var manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
                binding.recyclerview.layoutManager = manager
                binding.recyclerview.adapter = adapter

                binding.pgsbars.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "onCancelled: "+error.message)
            }
        })
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}