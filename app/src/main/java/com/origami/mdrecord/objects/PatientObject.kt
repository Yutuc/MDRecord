package com.origami.mdrecord.objects

class PatientObject(val uid: String, val firstName: String, val middleName: String, val lastName: String, val dateOfBirth: String, val gender: String, val bloodType: String, val civilStatus: String, val height: String, val weight: String, val nationality: String, val religion: String, val address: String, val email: String, val contactNumber: String, val diagnoses: String){
    constructor() : this("","","","","","","","","","","","","", "", "", "")
}