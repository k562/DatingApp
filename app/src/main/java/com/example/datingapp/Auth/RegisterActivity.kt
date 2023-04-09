package com.example.datingapp.Auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datingapp.MainActivity
import com.example.datingapp.Model.UserModel
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.example.datingapp.databinding.ActivityRegisterBinding
import com.example.datingapp.utils.Config
import com.example.datingapp.utils.Config.hideDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class RegisterActivity : AppCompatActivity() {

    lateinit var userImage : CircleImageView
    lateinit var userName : TextInputEditText
    lateinit var userEmail : TextInputEditText
    lateinit var userCity : TextInputEditText
    lateinit var termsConditions : CheckBox
    lateinit var saveData : Button
    private lateinit var binding: ActivityRegisterBinding

    private var imageUri : Uri? = null

    private val selectImage  = registerForActivityResult(ActivityResultContracts.GetContent()){

        imageUri = it

        binding.userImage.setImageURI(imageUri)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userImage = findViewById(R.id.userImage)
        userName = findViewById(R.id.userName)
        userEmail = findViewById(R.id.userEmail)
        userCity = findViewById(R.id.userCity)
        termsConditions = findViewById(R.id.termsConditions)
        saveData = findViewById(R.id.saveData)

           binding.userImage.setOnClickListener {

               selectImage.launch("image/*")
           }

        binding.saveData.setOnClickListener {
               validateData()
        }
    }

    private fun validateData() {
        if (binding.userName.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userCity.text.toString().isEmpty()
            || imageUri == null ){

            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()

        } else if (!binding.termsConditions.isChecked){

            Toast.makeText(this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show()

        }   else {

                uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

            storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                 storageRef.downloadUrl.addOnSuccessListener {
                     storeData(it)
                 } .addOnFailureListener {
                     hideDialog()
                     Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                 }
            } .addOnFailureListener {
                    hideDialog()

                    Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                }
    }

    private fun storeData(imageUrl: Uri?) {

        val data  = UserModel(

            name = binding.userName.text.toString(),
            image = imageUrl.toString(),
            email = binding.userEmail.text.toString(),
            city = binding.userCity.text.toString(),


        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener {

                hideDialog()

                if (it.isSuccessful){

                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "User Regisstered successfull",    Toast.LENGTH_SHORT).show()


              } else {

                    Toast.makeText(this, "${it.exception!!.message}",    Toast.LENGTH_SHORT).show()

                }
            }
    }
}