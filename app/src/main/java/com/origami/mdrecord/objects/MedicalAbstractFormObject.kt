package com.origami.mdrecord.objects

class MedicalAbstractFormObject(val patientName: String, val patientAge: String, val patientGender: String, val patientAddress: String, val medicalHistory: String, val phyisicalExamination: String, val diagnoses: String, val plans: String, val doctorName: String, val licenseNumber: String){
    constructor() : this("", "", "", "", "", "", "", "", "", "")
}