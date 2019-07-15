package com.origami.mdrecord.patientfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicineObject
import kotlinx.android.synthetic.main.activity_edit_medicine.*
import kotlinx.android.synthetic.main.activity_view_patient_file.*

class EditMedicineActivity : AppCompatActivity() {

    val medicineObject = MedicineFragment.medicineClicked!!.medicineObject
    val patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_medicine)
        setTitle("Edit ${MedicineFragment.medicineClicked!!.medicineObject.drugName}")
        displayInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_medicine_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_edit_medicine -> {
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
                    saveMedicine()
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

    private fun saveMedicine(){
        val drugName = drug_name_input_edit_medicine.text.toString().trim()
        val dose = dose_input_edit_medicine.text.toString().trim()
        val frequency = frequency_input_edit_medicine.text.toString().trim()
        val instructions = instructions_input_edit_medicine.text.toString().trim()

        var medicineArrayList = patientObject.medicineArrayList!!

        medicineArrayList.set(find(), MedicineObject(medicineObject.uid, drugName, dose, frequency, instructions))
        patientObject.medicineArrayList = medicineArrayList

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("medicineArrayList")
        ref.setValue(medicineArrayList)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully edited medicine", Toast.LENGTH_SHORT).show()
                ChoosePatientActivity.patientClicked!!.patientObject.medicineArrayList = medicineArrayList
                val intent = Intent(this, ViewPatientFileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //clears the stack of activities
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }//saveMedicine functions
    
    private fun find() : Int{
        val medicineArrayList = patientObject.medicineArrayList!!
        medicineArrayList.forEachIndexed { index, it ->
            if(it.uid == medicineObject.uid){
                return index
            }
        }
        return 0
    }//find function
}
