package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class chatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendBtn : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbref : DatabaseReference
//using sender and receiver room we create a unique room for sender and receiver
    var receiverRoom : String?=null
    var senderroom : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderId = FirebaseAuth.getInstance().currentUser?.uid
        mDbref = FirebaseDatabase.getInstance().getReference()


        receiverRoom = senderId + receiverRoom
        senderroom = senderId + receiverRoom

        supportActionBar?.title = name
        messageRecyclerView = findViewById(R.id.chatRecycler)
        messageBox = findViewById(R.id.messageBox)
        sendBtn = findViewById(R.id.sentBtn)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        messageRecyclerView.adapter = messageAdapter
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
//logic to show chats on recycleraview
        mDbref.child("chats").child(senderroom!!).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message ::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        sendBtn.setOnClickListener{
//            we have to send the message to db and then received by person
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderId)
//            we have one node of users ; we will create one more node of chats
//            push() creates a unique node
            mDbref.child("chats").child(senderroom!!).push().setValue(messageObject)
                .addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom!!).push().setValue(messageObject)
                }
//            to clear the messagebox
                messageBox.setText("")
        }
    }
}