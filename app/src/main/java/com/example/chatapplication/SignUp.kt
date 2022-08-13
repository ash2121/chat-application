package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_signup
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
//taking the reference of firebase db
    private lateinit var mDbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        btn_signupPage.setOnClickListener{
            val name = etName.text.toString()
            val email  = etEmail.text.toString()
            val password = etSPassword.text.toString()
            signUp(name,email,password)
        }
    }
    private fun signUp(name:String,email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    add user to database
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@SignUp,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUp,"Authentication failed",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDatabase(name : String, email : String, uid:String){
//            first we have to initialise db from firebase
//        we have to add gradle dependencies
        mDbRef = FirebaseDatabase.getInstance().getReference()
//        adding data to db
//        first we create a parent node of user
//        child will add node to the db
        mDbRef.child("user").child(uid).setValue((User(name,email,uid)))

    }
}