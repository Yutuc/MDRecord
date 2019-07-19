package com.origami.mdrecord.patientfile

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicalCertificateObject
import kotlinx.android.synthetic.main.activity_create_medical_certificate.*
import kotlinx.android.synthetic.main.confirm_assessment_alert_dialog.view.*
import java.text.DateFormat
import java.util.*

class CreateMedicalCertificateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_medical_certificate)
        setTitle("Create Medical Certificate")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_medical_certificate_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_medical_certificate -> {
                val nameOfAddressee = name_of_addressee_input_create_medical_certificate.text.toString().trim()
                val positionOfAddressee = position_of_addressee_input_create_medical_certificate.text.toString().trim()
                val diagnoses = diagnoses_input_create_medical_certificate.text.toString().trim()
                val remarks = remarks_input_create_medical_certificate.text.toString().trim()

                if(nameOfAddressee.isEmpty()){
                    Toast.makeText(this, "Please enter the name of the addressee", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(positionOfAddressee.isEmpty()){
                    Toast.makeText(this, "Please enter the position of the addressee", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(diagnoses.isEmpty()){
                    Toast.makeText(this, "Please enter the diagnoses", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(remarks.isEmpty()){
                    Toast.makeText(this, "Please enter the remarks", Toast.LENGTH_SHORT).show()
                    return true
                }
                else{
                    val dialogBuilder = AlertDialog.Builder(this)
                    val dialogView = layoutInflater.inflate(R.layout.confirm_assessment_alert_dialog, null)
                    dialogBuilder.setView(dialogView)

                    dialogView.confirm_message_textview_asessment_alert_dialog.text = "Once confirmed, you can't edit or delete an entry"

                    val alertDialog = dialogBuilder.create()!!
                    alertDialog.show()

                    dialogView.confirm_confirm_button_asessment_alert_dialog.setOnClickListener {
                        saveMedicalCertificate()
                        alertDialog.dismiss()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMedicalCertificate(){
        val currentUser = ViewPatientFileActivity.currentUser!!
        val currentPatient = ChoosePatientActivity.patientClicked!!.patientObject

        val doctorName = "${currentUser.firstName} ${currentUser.middleName.get(0)}. ${currentUser.lastName}"
        val licenseNumber = currentUser.licenseNumber
        val clinicAddress = currentUser.clinicAddress
        val contactNumber = currentUser.contactNumber

        val calendar = Calendar.getInstance()
        val date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)

        val nameOfAddressee = name_of_addressee_input_create_medical_certificate.text.toString().trim()
        val positionOfAddressee = position_of_addressee_input_create_medical_certificate.text.toString().trim()
        val patientName = "${currentPatient.firstName} ${currentPatient.middleName} ${currentPatient.lastName}"
        val patientAddress = currentPatient.address
        val diagnosis = diagnoses_input_create_medical_certificate.text.toString().trim()
        val remarks = remarks_input_create_medical_certificate.text.toString().trim()

        val medicalCertificateObject = MedicalCertificateObject(doctorName, licenseNumber, clinicAddress, contactNumber, date, nameOfAddressee, positionOfAddressee, patientName, patientAddress, diagnosis, remarks)

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${currentUser.uid}/${currentPatient.uid}").child("medical-certificate-history").push()
        ref.setValue(medicalCertificateObject)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved a medical certificate", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }//saveMedicalCertificate function
}
