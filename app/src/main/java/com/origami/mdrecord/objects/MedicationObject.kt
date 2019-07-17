package com.origami.mdrecord.objects

class MedicationObject(val uid: String, val drugName: String, val dose: String, val frequency: String, val instructions: String) {
    constructor() : this("", "", "", "" ,"")
}