package com.example.datingapp.Auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

   lateinit var userNumber : TextInputEditText
   lateinit var sendOtp : Button
   lateinit var userOtp : TextInputEditText
   lateinit var verifyOtp : Button
   lateinit var dialog: AlertDialog

    private lateinit var binding : ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    private var verificationId : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userNumber = findViewById(R.id.userNumber)
        sendOtp = findViewById(R.id.sendOtp)

        userOtp = findViewById(R.id.user0tp)
        verifyOtp = findViewById(R.id.verifyOtp)

        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)

            .setCancelable(false)
            .create()



        



        binding.sendOtp.setOnClickListener {

             if(binding.userNumber.text!!.isEmpty()){
                 binding.userNumber.error = "Please Enter your number"
             } else {
                 sendOtp(binding.userNumber.text.toString())
             }

        }

        binding.verifyOtp.setOnClickListener {

            if(binding.user0tp.text!!.isEmpty()){
                binding.user0tp.error = "Please Enter your otp"
            } else {
                verifyOtp(binding.user0tp.text.toString())
            }

        }


    }



    private fun verifyOtp(otp: String) {

     //   binding.BTn1.showLoadingButton()

        dialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)

      //  signInWithPhoneAuthCredential(credential)
        signInWithPhoneAuthCredential(credential)
    }



    private fun sendOtp(number: String) {


        //    binding.BTn1.showLoadingButton()

        dialog.show()
        val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {


               //     binding.BTn1.showNormalButton()
                    signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
              this@LoginActivity.verificationId = verificationId

             //   binding.userOtp.showNormalButton()

               dialog.dismiss()
                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility = VISIBLE

            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }





    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

              //  binding.BTn1.showNormalButton()
                if (task.isSuccessful) {

//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    finish()

                    checkUserExist(binding.userNumber.text.toString())

                } else {

                    dialog.dismiss()

                    Toast.makeText(this@LoginActivity, task.exception!!.message
                        , Toast.LENGTH_LONG).show()


                    }

                }
            }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+91$number")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0 : DatabaseError) {
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, p0.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if(p0.exists()){
                        dialog.dismiss()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                        finish()
                    }
                }
            })


    }
}
