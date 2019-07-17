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
import com.origami.mdrecord.objects.MedicationObject
import kotlinx.android.synthetic.main.activity_edit_medication.*

class EditMedicationActivity : AppCompatActivity() {

    val medicineObject = MedicationFragment.medicationClicked!!.medicationObject
    val patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medication)
        setTitle("Edit ${MedicationFragment.medicationClicked!!.medicationObject.drugName}")
        displayInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_medication_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_edit_medication -> {
                val drugName = drug_name_input_edit_medicine.text.toString().trim()
                val dose = dose_input_edit_medicine.text.toString().trim()
                val frequency = frequency_input_edit_medicine.text.toString().trim()
                val instructions = instructions_input_edit_medicine.text.toString().trim()

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
        drug_name_input_edit_medicine.setText(medicineObject.drugName)
        dose_input_edit_medicine.setText(medicineObject.dose)
        frequency_input_edit_medicine.setText(medicineObject.frequency)
        instructions_input_edit_medicine.setText(medicineObject.instructions)
    }//displayInfo function

    private fun saveMedication(){
        val drugName = drug_name_input_edit_medicine.text.toString().trim()
        val dose = dose_input_edit_medicine.text.toString().trim()
        val frequency = frequency_input_edit_medicine.text.toString().trim()
        val instructions = instructions_input_edit_medicine.text.toString().trim()

        var medicineArrayList = patientObject.medicationArrayList!!

        medicineArrayList.set(find(), MedicationObject(medicineObject.uid, drugName, dose, frequency, instructions))
        patientObject.medicationArrayList = medicineArrayList

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("medicationArrayList")
        ref.setValue(medicineArrayList)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully edited medicine", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }//saveMedicine functions
    
    private fun find() : Int{
        val medicineArrayList = patientObject.medicationArrayList!!
        medicineArrayList.forEachIndexed { index, it ->
            if(it.uid == medicineObject.uid){
                return index
            }
        }
        return 0
    }//find function
}
