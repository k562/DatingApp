package com.example.datingapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.datingapp.Adapter.MessageAdapter
import com.example.datingapp.Model.MessageModel
import com.example.datingapp.databinding.ActivityMessageAcivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class MessageAcivity : AppCompatActivity() {

//    lateinit var yourmessage : TextInputEditText
//    lateinit var imageview4 : ImageView
    private lateinit var binding: ActivityMessageAcivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageAcivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


       // getData(intent.getStringExtra("chat_Id"))

        verifyChatId()

        binding.imageViewsendmesaage.setOnClickListener {

            if (binding.yourMessage.text!!.isEmpty()){
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            } else {
                storeData(binding.yourMessage.text.toString())
            }
        }


    }

    private var senderId : String ?= null
    private var  chatId : String ?= null

    private fun verifyChatId() {

        val recieverId  = intent.getStringExtra("userId")
        senderId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

         chatId = senderId + recieverId
        val reverseChatId = recieverId + senderId



        val reference = FirebaseDatabase.getInstance().getReference("chats")

        //    reference.child(reference.key!!).setValue(map).addOnCompleteListener {

        reference . addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.hasChild(chatId!!)){
                  //  storeData(reverseChatId , msg , senderId)
                    getData(chatId)
                } else if(snapshot.hasChild(reverseChatId)) {
                    // storeData(chatId , msg , senderId)
                    chatId = reverseChatId
                    getData(chatId)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageAcivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getData(chatID: String?) {

        FirebaseDatabase.getInstance().getReference("chats").child(chatID!!)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val list = arrayListOf<MessageModel>()

                for (show in snapshot.children){
                    list.add(show.getValue(MessageModel::class.java)!!)
                }

                binding.recyclerView3.adapter = MessageAdapter(this@MessageAcivity, list)
            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@MessageAcivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }



    private fun storeData( msg: String) {

        val currentDate : String  = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentTime : String  = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())


        val map  = hashMapOf<String,String>()
        map["message"] = msg
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate


        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)


        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {

            if(it.isSuccessful){
                binding.yourMessage.text = null
                Toast.makeText(this, "Message Sended", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

