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
import kotlinx.android.synthetic.main.activity_add_medicine.*
import java.util.ArrayList

class AddMedicineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicine)
        setTitle("Add Medicine")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_medicine_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_medicine -> {
                val drugName = drug_name_input_medicine.text.toString().trim()
                val dose = dose_input_medicine.text.toString().trim()
                val frequency = frequency_input_medicine.text.toString().trim()
                val instructions = instructions_input_medicine.text.toString().trim()

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

    private fun saveMedicine(){
        val drugName = drug_name_input_medicine.text.toString().trim()
        val dose = dose_input_medicine.text.toString().trim()
        val frequency = frequency_input_medicine.text.toString().trim()
        val instructions = instructions_input_medicine.text.toString().trim()

        var medicineArrayList: ArrayList<MedicineObject>? = arrayListOf()

        if(ChoosePatientActivity.patientClicked!!.patientObject.medicineArrayList!! != null) {
            medicineArrayList = ChoosePatientActivity.patientClicked!!.patientObject.medicineArrayList!!
        }

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("medicineArrayList")
        medicineArrayList!!.add(MedicineObject(medicineArrayList.size.toString(), drugName, dose, frequency, instructions))
        ref.setValue(medicineArrayList)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully added medicine", Toast.LENGTH_SHORT).show()
                ChoosePatientActivity.patientClicked!!.patientObject.medicineArrayList = medicineArrayList
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }//saveMedicine function
}
