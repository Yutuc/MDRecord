package com.origami.mdrecord.objects

class MedicalCertificateObject (val doctorName: String, val licenseNumber: String, val clinicAddress: String, val contactNumber: String, val date: String, val nameOfAddresee: String, val positionOfAddresee: String, val patientName: String, val patientAddress: String, val diagnosis: String, val remarks: String){
    constructor() : this("", "","", "", "", "", "", "", "", "", "")
}
