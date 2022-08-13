package com.example.chatapplication

import androidx.annotation.Nullable

class User {
//    firebase requires an constructor to work with therefore we are not making data class
    var name : String?=null
    var email: String ?=null
    var uid : String?=null

    constructor(){}
    constructor(name:String?,email:String?,uid:String?){
        this.name = name
        this.email = email
        this.uid = uid
    }
}
