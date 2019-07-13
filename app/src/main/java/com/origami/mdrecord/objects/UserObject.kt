package com.origami.mdrecord.objects

class UserObject(val uid: String, val firstName: String, val lastName: String, val email: String, val password: String){
    constructor() : this("", "", "", "", "")
}