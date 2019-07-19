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
import kotlinx.android.synthetic.main.activity_add_patient_medication.*

class AddPatientMedicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient_medication)
        setTitle("Add Patient's Medication")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_patient_medication_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_patient_medication -> {
                val drugName = drug_name_input_patient_medication.text.toString().trim()
                val dose = dose_input_patient_medication.text.toString().trim()
                val frequency = frequency_input_patient_medication.text.toString().trim()
                val instructions = instructions_input_patient_medication.text.toString().trim()

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
                    savePatientMedication()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePatientMedication(){
        val drugName = drug_name_input_patient_medication.text.toString().trim()
        val dose = dose_input_patient_medication.text.toString().trim()
        val frequency = frequency_input_patient_medication.text.toString().trim()
        val instructions = instructions_input_patient_medication.text.toString().trim()

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}/patient-medication").push()
        ref.setValue(PatientMedicationObject(ref.key!!, drugName, dose, frequency, instructions))
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully added medication", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }//savepatient_medication function
}
