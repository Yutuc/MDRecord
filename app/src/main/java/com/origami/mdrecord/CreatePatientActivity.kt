package com.origami.mdrecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.objects.PatientObject
import kotlinx.android.synthetic.main.activity_create_patient.*

class CreatePatientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_patient)
        setTitle("Create a patient")

        height_feet_input_create_patient.visibility = View.INVISIBLE
        height_inches_input_create_patient.visibility = View.INVISIBLE

        height_unit_of_measurement_radio_group_create_patient.setOnCheckedChangeListener { _, i ->
            val checkedRadioButton = findViewById<RadioButton>(i).text.toString().trim()
            if(checkedRadioButton == "Meters"){
                height_meter_input_create_patient.visibility = View.VISIBLE
                height_feet_input_create_patient.visibility = View.INVISIBLE
                height_inches_input_create_patient.visibility = View.INVISIBLE
                height_feet_input_create_patient.setText("")
                height_inches_input_create_patient.setText("")
            }
            else{
                height_meter_input_create_patient.visibility = View.INVISIBLE
                height_feet_input_create_patient.visibility = View.VISIBLE
                height_inches_input_create_patient.visibility = View.VISIBLE
                height_meter_input_create_patient.setText("")
            }
        }

        weight_unit_of_measurement_radio_group_create_patient.setOnCheckedChangeListener { _, i ->
            val checkedRadioButton = findViewById<RadioButton>(i).text.toString().trim()
            if(checkedRadioButton == "Kilograms"){
                weight_input_create_patient.hint = "Weight(kg)"
            }
            else{
                weight_input_create_patient.hint = "Weight(lbs)"
            }
            weight_input_create_patient.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_patient_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_patient -> {
                savePatient()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePatient(){
        val firstName = first_name_input_create_patient.text.toString().trim()
        val middleName = middle_name_input_create_patient.text.toString().trim()
        val lastName = last_name_input_create_patient.text.toString().trim()
        val dateOfBirth = date_of_birth_input_create_patient.text.toString().trim()
        val genderSelectedId = gender_radio_group_create_patient.checkedRadioButtonId
        val bloodTypeSelectedId = blood_type_radio_group_create_patient.checkedRadioButtonId
        val civilStatusSelectedId = civil_status_radio_group_create_patient.checkedRadioButtonId

        val heightUnitSelectedId = height_unit_of_measurement_radio_group_create_patient.checkedRadioButtonId
        val weightUnitSelectedId = weight_unit_of_measurement_radio_group_create_patient.checkedRadioButtonId
        val heightUnitSelected = findViewById<RadioButton>(heightUnitSelectedId).text.toString().trim()
        val weightUnitSelected = findViewById<RadioButton>(weightUnitSelectedId).text.toString().trim()

        val nationality = nationality_input_create_patient.text.toString().trim()
        val religion = religion_input_create_patient.text.toString().trim()
        val address = address_input_create_patient.text.toString().trim()
        val email = email_input_create_patient.text.toString().trim()
        val contactNumber = contact_number_input_create_patient.text.toString().trim()
        var diagnoses = diagnoses_input_create_patient.text.toString().trim()

        if(firstName.isEmpty()){
            Toast.makeText(this, "Please enter the patient's first name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(middleName.isEmpty()){
            Toast.makeText(this, "Please enter the patient's middle name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(lastName.isEmpty()){
            Toast.makeText(this, "Please enter the patient's last name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(dateOfBirth.isEmpty()){
            Toast.makeText(this, "Please enter the patient's date of birth", Toast.LENGTH_SHORT).show()
            return
        }
        else if(genderSelectedId == -1){
            Toast.makeText(this, "Please select the patient's gender", Toast.LENGTH_SHORT).show()
            return
        }
        else if(bloodTypeSelectedId == -1){
            Toast.makeText(this, "Please select the patient's blood type", Toast.LENGTH_SHORT).show()
            return
        }
        else if(civilStatusSelectedId == -1){
            Toast.makeText(this, "Please select the patient's civil status", Toast.LENGTH_SHORT).show()
            return
        }
        else if(heightUnitSelected == "Meters" && height_meter_input_create_patient.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter the patient's height", Toast.LENGTH_SHORT).show()
            return
        }
        else if(heightUnitSelected == "Feet & Inches"){
            if(height_feet_input_create_patient.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please enter feet for patient's height", Toast.LENGTH_SHORT).show()
                return
            }
            else if(height_inches_input_create_patient.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please enter inches for patient's height", Toast.LENGTH_SHORT).show()
                return
            }
        }
        else if(weight_input_create_patient.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter the patient's weight", Toast.LENGTH_SHORT).show()
            return
        }
        else if(nationality.isEmpty()){
            Toast.makeText(this, "Please enter the patient's nationality", Toast.LENGTH_SHORT).show()
            return
        }
        else if(religion.isEmpty()){
            Toast.makeText(this, "Please enter the patient's religion", Toast.LENGTH_SHORT).show()
            return
        }
        else if(address.isEmpty()){
            Toast.makeText(this, "Please enter the patient's address", Toast.LENGTH_SHORT).show()
            return
        }
        else if(email.isEmpty()){
            Toast.makeText(this, "Please enter the patient's email", Toast.LENGTH_SHORT).show()
            return
        }
        else if(contactNumber.isEmpty()){
            Toast.makeText(this, "Please enter the patient's contact number", Toast.LENGTH_SHORT).show()
            return
        }
        else if(diagnoses.isEmpty()){
            diagnoses = ""
        }

        val gender = findViewById<RadioButton>(genderSelectedId).text.toString().trim()
        val bloodType = findViewById<RadioButton>(bloodTypeSelectedId).text.toString().trim()
        val civilStatus = findViewById<RadioButton>(civilStatusSelectedId).text.toString().trim()
        var height: String?
        var weight: String?

        if(heightUnitSelected == "Meters"){
            height = "${height_meter_input_create_patient.text.toString().trim()}"
        }
        else{
            height = "${height_feet_input_create_patient.text.toString().trim()} ${height_inches_input_create_patient.text.toString().trim()}"
        }

        if(weightUnitSelected == "Kilograms"){
            weight = "${weight_input_create_patient.text.toString().trim()} Kilograms"
        }
        else{
            weight = "${weight_input_create_patient.text.toString().trim()} Lbs"
        }

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}").push()
        ref.setValue(
            PatientObject(
                ref.key!!,
                firstName,
                middleName,
                lastName,
                dateOfBirth,
                gender,
                bloodType,
                civilStatus,
                height,
                weight,
                nationality,
                religion,
                address,
                email,
                contactNumber,
                diagnoses
            )
        )
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully created patient", Toast.LENGTH_SHORT).show()
            }
        finish()
    }//savePatient function
}
