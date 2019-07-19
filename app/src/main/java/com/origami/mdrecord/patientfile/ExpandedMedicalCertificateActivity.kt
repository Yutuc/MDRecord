package com.origami.mdrecord.patientfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.origami.mdrecord.R
import kotlinx.android.synthetic.main.activity_expanded_medical_certificate.*

class ExpandedMedicalCertificateActivity : AppCompatActivity() {

    val medicalCertificateClicked = MedicalCertificateHistoryActivity.medicalCertificateClicked!!.medicalCertificateObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_medical_certificate)
        setTitle("Full medical certificate")
        displayInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.expanded_medical_certificate_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.convert_to_pdf -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayInfo(){
        doctor_name_textview_expanded_medical_certificate.text = medicalCertificateClicked.doctorName
        clinic_address_expanded_medical_certificate.text = medicalCertificateClicked.clinicAddress
        contact_number_textview_expanded_medical_certificate.text = "Contact number: ${medicalCertificateClicked.contactNumber}"
        date_textview_expanded_medical_certificate.text = "Date: ${medicalCertificateClicked.date}"
        name_of_addressee_expanded_medical_certificate.text = medicalCertificateClicked.nameOfAddresee
        position_of_addressee_expanded_medical_textview.text = medicalCertificateClicked.positionOfAddresee

        main_paragraph_textview_expanded_medical_certificate.text = "This is to certify that ${medicalCertificateClicked.patientName} " +
                "of, \n${medicalCertificateClicked.patientAddress}, was examined and treated by the undersigned on ${medicalCertificateClicked.date} " +
                "with the following diagnosis."

        diagnosis_textview_expanded_medical_certificate.text = medicalCertificateClicked.diagnosis
        remarks_textview_expanded_medical_certificate.text = medicalCertificateClicked.remarks

        name_of_doctor_bottom_expanded_medical_certificate.text = medicalCertificateClicked.doctorName
        license_number_bottom.text = "License nubmer: ${medicalCertificateClicked.licenseNumber}"
    }//displayInfo function
}
