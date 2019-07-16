package com.origami.mdrecord.patientfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import kotlinx.android.synthetic.main.activity_expanded_patient_info.*
import java.util.*

class ExpandedPatientInfoActivity : AppCompatActivity() {

    val patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_patient_info)
        setTitle("${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName}'s Info")
        displayPatientInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.expanded_patient_info_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_patient -> {
                val intent = Intent(this, EditPatientActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayPatientInfo(){
        name_age_gender_textview_expanded_patient_info.text = "${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName} ${getAge()}/${getGender()}"
        address_textview_expanded_patient_info.text = "Address: ${ChoosePatientActivity.patientClicked?.patientObject?.address}"
        contact_number_textview_expanded_patient_info.text = "Contact number: ${patientObject.contactNumber}"
        email_textview_expanded_patient_info.text = "Email: ${patientObject.email}"
        if(ChoosePatientActivity.patientClicked?.patientObject?.diagnoses?.isEmpty()!!){
            diagnoses_textview_expanded_patient_info.text = "N/A"
        }
        else{
            diagnoses_textview_expanded_patient_info.text = "${patientObject.diagnoses}"
        }
        date_of_birth_textview_expanded_patient_info.text = "Date of birth: ${patientObject.dateOfBirth}"
        civil_status_textview_expanded_patient_info.text = "Civil status: ${patientObject.civilStatus}"
        blood_type_textview_expanded_patient_info.text = "Blood type: ${patientObject.bloodType}"

        val heightString = patientObject.height.split(Regex("\\s+"))
        var height: String?
        var bmiHeight: Float
        if(heightString.size == 1){
            height = "${heightString[0]}"
            bmiHeight = heightString[0].toFloat()
            height += " m"
        }
        else{
            height = "${heightString[0]}' ${heightString[1]}\""
            bmiHeight = (heightString[0].toFloat()/3.281.toFloat()) + (heightString[1].toFloat()/39.37.toFloat())
        }

        val weightString = patientObject.weight.split(Regex("\\s+"))
        val weight: String?
        val bmiWeight: Float
        if(weightString.contains("Kilograms")){
            bmiWeight = weightString.get(0).toFloat()
            weight = "${weightString[0]} Kg"
        }
        else{
            bmiWeight = (weightString.get(0).toFloat() / (2.205).toFloat())
            weight = "${weightString[0]} Lbs"
        }

        height_textview_expanded_patient_info.text = "Height: $height"
        weight_textview_expanded_patient_info.text = "Weight: ${weight}"
        val formattedBMI = "%.2f".format(getBMI(bmiWeight, bmiHeight))
        bmi_textview_expanded_patient_info.text = "BMI: $formattedBMI"
        nationality_textview_expanded_patient_info.text = "Nationality: ${patientObject.nationality}"
        religion_textview_expanded_patient_info.text = "Religion: ${patientObject.religion}"
    }//displayPatientInfo function

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

    private fun getBMI(bmiWeight: Float, bmiHeight: Float) : Float{
        return (bmiWeight / (bmiHeight * bmiHeight))
    }//getBMI function
}
