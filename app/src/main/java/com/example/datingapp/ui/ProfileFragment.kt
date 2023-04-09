package com.example.datingapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.datingapp.Activity.EditProfile_Acivity
import com.example.datingapp.Auth.LoginActivity
import com.example.datingapp.Model.UserModel
import com.example.datingapp.R
import com.example.datingapp.databinding.FragmentDatingBinding
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.utils.Config
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


       Config.showDialog(requireActivity() )

       binding = FragmentProfileBinding.inflate(layoutInflater)



        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Handle the snapshot data here
                        val data = snapshot.getValue(UserModel::class.java)
                        binding.name.setText(data!!.name.toString())
                        binding.city.setText(data.city.toString())
                        binding.email.setText(data.email.toString())
                        binding.number.setText(data.number.toString())


                        Glide.with(requireContext()).load(data.image).placeholder(R.drawable.profile).into(binding.userImage)

                        Config.hideDialog()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors here
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })


        binding.logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()

        }

        binding.editProfile.setOnClickListener {

            startActivity(Intent(requireContext(), EditProfile_Acivity::class.java))

        }
        return binding.root
    }


}




