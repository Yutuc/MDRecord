package com.origami.mdrecord.objects

class UserObject(val uid: String, val firstName: String, val middleName: String, val lastName: String, val specialty: String, val licenseNumber: String, val insuranceProviderNumber: String, val s2Number: String, val clinicAddress: String, val phoneNumber: String, val email: String, val password: String){
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "")
}