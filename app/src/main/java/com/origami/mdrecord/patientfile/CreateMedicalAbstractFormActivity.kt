package com.origami.mdrecord.patientfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicalAbstractFormObject
import kotlinx.android.synthetic.main.activity_create_medical_abstract_form.*
import java.text.DateFormat
import java.util.*

class CreateMedicalAbstractFormActivity : AppCompatActivity() {

    val patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_medical_abstract_form)
        setTitle("Create Medical Abstract Form")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_medical_abstract_form_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_medical_abstract_form -> {
                val medicalAssessment = medical_history_input_create_medical_abstract_form.text.toString().trim()
                val physicalExamination = physical_examination_input_create_medical_abstract_form.text.toString().trim()
                val diagnoses = diagnoses_input_create_medical_abstract_form.text.toString().trim()
                val plans = plans_input_create_medical_abstract_form.text.toString().trim()

                if(medicalAssessment.isEmpty()){
                    Toast.makeText(this, "Please enter the current medical assessment", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(physicalExamination.isEmpty()){
                    Toast.makeText(this, "Please enter the current physical examination", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(diagnoses.isEmpty()){
                    Toast.makeText(this, "Please enter the diagnoses", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(plans.isEmpty()){
                    Toast.makeText(this, "Please enter the plans", Toast.LENGTH_SHORT).show()
                    return true
                }
                else{
                    viewPDF()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun viewPDF(){

        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)

        val patientName = "${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName}"
        val patientAge = getAge()
        val patientGender = getGender()
        val patientAddress = patientObject.address

        val medicalHistory = medical_history_input_create_medical_abstract_form.text.toString().trim()
        val physicalExamination = physical_examination_input_create_medical_abstract_form.text.toString().trim()
        val diagnoses = diagnoses_input_create_medical_abstract_form.text.toString().trim()
        val plans = plans_input_create_medical_abstract_form.text.toString().trim()

        val currentUser = ViewPatientFileActivity.currentUser!!
        val doctorName = "${currentUser.firstName} ${currentUser.middleName.get(0)}. ${currentUser.lastName}"
        val licenseNumber = currentUser.licenseNumber

        val medicalAbstractFormObject= MedicalAbstractFormObject(date, patientName, patientAge, patientGender, patientAddress, medicalHistory, physicalExamination, diagnoses, plans, doctorName, licenseNumber)
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${currentUser.uid}/${patientObject.uid}").child("medical-abstract-form-history").push()
        ref.setValue(medicalAbstractFormObject)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved a medical abstract form", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }//viewPDF function

    private fun getAge() : String{
        val dateOfBirthSplit = patientObject.dateOfBirth.split("/")
        val birthYear = dateOfBirthSplit.get(dateOfBirthSplit.size-1).toInt()
        val currentyear = Calendar.getInstance().get(Calendar.YEAR)
        return (currentyear-birthYear).toString()
    }//getAge function

    private fun getGender() : String{
        if(patientObject.gender == "Male"){
            return "M"
        }
        else{
            return "F"
        }
    }//getGender function
}
