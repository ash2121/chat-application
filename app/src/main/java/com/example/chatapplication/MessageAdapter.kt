package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context,val messageList:ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    we need 2 viewHolders : one for recieving msg and other for sending the msg
val ITEM_RECEIVE = 1
    val ITEM_SENT =2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//different layouts for sent and received
//        to get a particular view, we have to override another method
        if(viewType == 1){
//            inflate received
            val view : View = LayoutInflater.from(context).inflate(R.layout.received_msg_layout,parent,false)
            return receivedViewHolder(view)
        }
        else{
//            inflate sent
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent_layout,parent,false)
            return sentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == sentViewHolder :: class.java){
//            sent msg
//            typecast holder to sentviewHolder


            val viewHolder = holder as sentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else{
                val viewHolder = holder as receivedViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentmessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentmessage.senderId)){
            return ITEM_RECEIVE
        }
        else{
            return ITEM_SENT
        }

    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    class sentViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
            val sentMessage = itemView.findViewById<TextView>(R.id.txt_sentMsg)
    }
    class receivedViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
            val receivedMessage = itemView.findViewById<TextView>(R.id.txt_receivedMsg)
    }
}