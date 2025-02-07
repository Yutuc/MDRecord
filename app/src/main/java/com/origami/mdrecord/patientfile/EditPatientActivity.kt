package com.origami.mdrecord.patientfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.PatientObject
import kotlinx.android.synthetic.main.activity_edit_patient.*

class EditPatientActivity : AppCompatActivity() {

    val patientObject = ExpandedPatientInfoActivity.patientObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient)
        setTitle("Edit ${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName}")

        height_feet_input_edit_patient.visibility = View.INVISIBLE
        height_inches_input_edit_patient.visibility = View.INVISIBLE

        height_unit_of_measurement_radio_group_edit_patient.setOnCheckedChangeListener { _, i ->
            val checkedRadioButton = findViewById<RadioButton>(i).text.toString().trim()
            if(checkedRadioButton == "Meters"){
                height_meter_input_edit_patient.visibility = View.VISIBLE
                height_feet_input_edit_patient.visibility = View.INVISIBLE
                height_inches_input_edit_patient.visibility = View.INVISIBLE
                height_feet_input_edit_patient.setText("")
                height_inches_input_edit_patient.setText("")
            }
            else{
                height_meter_input_edit_patient.visibility = View.INVISIBLE
                height_feet_input_edit_patient.visibility = View.VISIBLE
                height_inches_input_edit_patient.visibility = View.VISIBLE
                height_meter_input_edit_patient.setText("")
            }
        }

        weight_unit_of_measurement_radio_group_edit_patient.setOnCheckedChangeListener { _, i ->
            val checkedRadioButton = findViewById<RadioButton>(i).text.toString().trim()
            if(checkedRadioButton == "Kilograms"){
                weight_input_edit_patient.hint = "Weight(Kg)"
            }
            else{
                weight_input_edit_patient.hint = "Weight(lbs)"
            }
            weight_input_edit_patient.setText("")
        }

        displayInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_patient_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_edit_patient -> {
                savePatient()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayInfo(){
        first_name_input_edit_patient.setText(patientObject.firstName)
        middle_name_input_edit_patient.setText(patientObject.middleName)
        last_name_input_edit_patient.setText(patientObject.lastName)
        date_of_birth_input_edit_patient.setText(patientObject.dateOfBirth)

        if(patientObject.gender == "Male"){
            gender_radio_group_edit_patient.check(R.id.male_radio_button_edit_patient)
        }
        else{
            gender_radio_group_edit_patient.check(R.id.female_radio_button_edit_patient)
        }

        if(patientObject.bloodType == "A+"){
            blood_type_radio_group_edit_patient.check(R.id.a_positive_radio_button_edit_patient)
        }
        else if(patientObject.bloodType == "A-"){
            blood_type_radio_group_edit_patient.check(R.id.a_negative_radio_button_edit_patient)
        }
        else if(patientObject.bloodType == "B+"){
            blood_type_radio_group_edit_patient.check(R.id.b_positive_radio_button_edit_patient)
        }
        else if(patientObject.bloodType == "B-"){
            blood_type_radio_group_edit_patient.check(R.id.b_negative_radio_button_edit_patient)
        }
        else if(patientObject.bloodType == "AB+"){
            blood_type_radio_group_edit_patient.check(R.id.ab_positive_radio_button_edit_patient)
        }
        else if(patientObject.bloodType == "AB-"){
            blood_type_radio_group_edit_patient.check(R.id.ab_negative_radio_button_edit_patient)
        }
        else if(patientObject.bloodType == "O+"){
            blood_type_radio_group_edit_patient.check(R.id.o_positive_radio_button_edit_patient)
        }
        else{
            blood_type_radio_group_edit_patient.check(R.id.o_negative_radio_button_edit_patient)
        }

        if(patientObject.civilStatus == "Single"){
            civil_status_radio_group_edit_patient.check(R.id.single_radio_button_edit_patient)
        }
        else if(patientObject.civilStatus == "Married"){
            civil_status_radio_group_edit_patient.check(R.id.married_radio_button_edit_patient)
        }
        else if(patientObject.civilStatus == "Separated"){
            civil_status_radio_group_edit_patient.check(R.id.separated_radio_button_edit_patient)
        }
        else{
            civil_status_radio_group_edit_patient.check(R.id.divorced_radio_button_edit_patient)
        }

        val heightString = patientObject.height.split(Regex("\\s+"))
        if(heightString.size == 1){
            meters_radio_button_edit_patient.isChecked = true
            height_meter_input_edit_patient.visibility = View.VISIBLE
            height_feet_input_edit_patient.visibility = View.INVISIBLE
            height_inches_input_edit_patient.visibility = View.INVISIBLE
            height_feet_input_edit_patient.setText("")
            height_inches_input_edit_patient.setText("")
            height_meter_input_edit_patient.setText("${heightString.get(0)}")
        }
        else{
            feet_and_inches_radio_button_edit_patient.isChecked = true
            height_meter_input_edit_patient.visibility = View.INVISIBLE
            height_feet_input_edit_patient.visibility = View.VISIBLE
            height_inches_input_edit_patient.visibility = View.VISIBLE
            height_meter_input_edit_patient.setText("")
            height_feet_input_edit_patient.setText("${heightString.get(0)}")
            height_inches_input_edit_patient.setText("${heightString.get(1)}")
        }

        val weightString = patientObject.weight.split(Regex("\\s+"))
        if(weightString.contains("Kilograms")){
            kilograms_radio_button_edit_patient.isChecked = true
            weight_input_edit_patient.hint = "Weight(Kg)"
            weight_input_edit_patient.setText(weightString.get(0))
        }
        else{
            pounds_radio_button_edit_patient.isChecked = true
            weight_input_edit_patient.hint = "Weight(Lbs)"
            weight_input_edit_patient.setText(weightString.get(0))
        }

        nationality_input_edit_patient.setText(patientObject.nationality)
        religion_input_edit_patient.setText(patientObject.religion)
        address_input_edit_patient.setText(patientObject.address)
        email_input_edit_patient.setText(patientObject.email)
        contact_number_input_edit_patient.setText(patientObject.contactNumber)
        diagnoses_input_edit_patient.setText(patientObject.diagnoses)
    }//displayInfo function

    private fun savePatient(){
        val firstName = first_name_input_edit_patient.text.toString().trim()
        val middleName = middle_name_input_edit_patient.text.toString().trim()
        val lastName = last_name_input_edit_patient.text.toString().trim()
        val dateOfBirth = date_of_birth_input_edit_patient.text.toString().trim()
        val genderSelectedId = gender_radio_group_edit_patient.checkedRadioButtonId
        val bloodTypeSelectedId = blood_type_radio_group_edit_patient.checkedRadioButtonId
        val civilStatusSelectedId = civil_status_radio_group_edit_patient.checkedRadioButtonId

        val heightUnitSelectedId = height_unit_of_measurement_radio_group_edit_patient.checkedRadioButtonId
        val weightUnitSelectedId = weight_unit_of_measurement_radio_group_edit_patient.checkedRadioButtonId
        val heightUnitSelected = findViewById<RadioButton>(heightUnitSelectedId).text.toString().trim()
        val weightUnitSelected = findViewById<RadioButton>(weightUnitSelectedId).text.toString().trim()

        val nationality = nationality_input_edit_patient.text.toString().trim()
        val religion = religion_input_edit_patient.text.toString().trim()
        val address = address_input_edit_patient.text.toString().trim()
        val email = email_input_edit_patient.text.toString().trim()
        val contactNumber = contact_number_input_edit_patient.text.toString().trim()
        var diagnoses = diagnoses_input_edit_patient.text.toString().trim()

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
        else if(heightUnitSelected == "Meters" && height_meter_input_edit_patient.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter the patient's height", Toast.LENGTH_SHORT).show()
            return
        }
        else if(heightUnitSelected == "Feet & Inches"){
            if(height_feet_input_edit_patient.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please enter feet for patient's height", Toast.LENGTH_SHORT).show()
                return
            }
            else if(height_inches_input_edit_patient.text.toString().trim().isEmpty()){
                Toast.makeText(this, "Please enter inches for patient's height", Toast.LENGTH_SHORT).show()
                return
            }
        }
        else if(weight_input_edit_patient.text.toString().trim().isEmpty()){
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
            height = "${height_meter_input_edit_patient.text.toString().trim()}"
        }
        else{
            height = "${height_feet_input_edit_patient.text.toString().trim()} ${height_inches_input_edit_patient.text.toString().trim()}"
        }

        if(weightUnitSelected == "Kilograms"){
            weight = "${weight_input_edit_patient.text.toString().trim()} Kilograms"
        }
        else{
            weight = "${weight_input_edit_patient.text.toString().trim()} Lbs"
        }

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${patientObject.uid}")

        if(firstName != patientObject.firstName){
            ref.child("firstName").setValue(firstName)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(middleName != patientObject.middleName){
            ref.child("middleName").setValue(middleName)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(lastName != patientObject.lastName){
            ref.child("lastName").setValue(lastName)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(dateOfBirth != patientObject.dateOfBirth){
            ref.child("dateOfBirth").setValue(dateOfBirth)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(gender != patientObject.gender){
            ref.child("gender").setValue(gender)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(bloodType != patientObject.bloodType){
            ref.child("bloodType").setValue(bloodType)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(civilStatus != patientObject.civilStatus){
            ref.child("civilStatus").setValue(civilStatus)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(height != patientObject.height){
            ref.child("height").setValue(height)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(weight != patientObject.weight){
            ref.child("weight").setValue(weight)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(nationality != patientObject.nationality){
            ref.child("nationality").setValue(nationality)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(religion != patientObject.religion){
            ref.child("religion").setValue(religion)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(address != patientObject.address){
            ref.child("address").setValue(address)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(email != patientObject.email){
            ref.child("email").setValue(email)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(contactNumber != patientObject.contactNumber){
            ref.child("contactNumber").setValue(contactNumber)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        if(diagnoses != patientObject.diagnoses){
            ref.child("diagnoses").setValue(diagnoses)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully edited patient", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }//savePatient function
}
