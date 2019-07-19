package com.origami.mdrecord.patientfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.PatientMedicationObject
import kotlinx.android.synthetic.main.activity_edit_patient_medication.*

class EditPatientMedicationActivity : AppCompatActivity() {

    val patientMedicationObject = PatientMedicationFragment.patientMedicationClicked!!.patientMedicationObject
    val patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient_medication)
        setTitle("Edit ${PatientMedicationFragment.patientMedicationClicked!!.patientMedicationObject.drugName}")
        displayInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_patient_medication_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_edit_patient_medication -> {
                val drugName = drug_name_input_edit_patient_medication.text.toString().trim()
                val dose = dose_input_edit_patient_medication.text.toString().trim()
                val frequency = frequency_input_edit_patient_medication.text.toString().trim()
                val instructions = instructions_input_edit_patient_medication.text.toString().trim()

                if(drugName.isEmpty()){
                    Toast.makeText(this, "Please enter the drug's name", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(dose.isEmpty()){
                    Toast.makeText(this, "Please enter the dose", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(frequency.isEmpty()){
                    Toast.makeText(this, "Please enter the frequency", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(instructions.isEmpty()){
                    Toast.makeText(this, "Please enter the instructions", Toast.LENGTH_SHORT).show()
                    return true
                }
                else{
                    saveMedication()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayInfo(){
        drug_name_input_edit_patient_medication.setText(patientMedicationObject.drugName)
        dose_input_edit_patient_medication.setText(patientMedicationObject.dose)
        frequency_input_edit_patient_medication.setText(patientMedicationObject.frequency)
        instructions_input_edit_patient_medication.setText(patientMedicationObject.instructions)
    }//displayInfo function

    private fun saveMedication(){
        val drugName = drug_name_input_edit_patient_medication.text.toString().trim()
        val dose = dose_input_edit_patient_medication.text.toString().trim()
        val frequency = frequency_input_edit_patient_medication.text.toString().trim()
        val instructions = instructions_input_edit_patient_medication.text.toString().trim()

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}/patient-medication").child("${patientMedicationObject.uid}")
        ref.setValue(PatientMedicationObject(patientMedicationObject.uid, drugName, dose, frequency, instructions))
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully edited patient medication", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }//savepatient_medication functions
}
