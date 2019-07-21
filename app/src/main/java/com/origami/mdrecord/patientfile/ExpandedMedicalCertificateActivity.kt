package com.origami.mdrecord.patientfile

import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import com.origami.mdrecord.R
import kotlinx.android.synthetic.main.activity_expanded_medical_certificate.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

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
                createPDF()
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
        license_number_bottom.text = "License number: ${medicalCertificateClicked.licenseNumber}"
    }//displayInfo function

    private fun createPDF(){
        val width = constraint_layout_scrollview_expanded_medical_certificate.measuredWidth
        val height = constraint_layout_scrollview_expanded_medical_certificate.measuredHeight
        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()

        //creates a page
        val page = document.startPage(pageInfo)
        val content = constraint_layout_scrollview_expanded_medical_certificate
        content.draw(page.canvas)
        document.finishPage(page)

        //multiple pages can be created the same way here

        val path = Environment.getExternalStorageDirectory().absolutePath + "/medicalCertificate.pdf"
        val directory = File(path)

        try {
            document.writeTo(FileOutputStream(directory))
        }
        catch(e: Exception){
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

        document.close()
        openGeneratedPDF()
    }//createPDF function

    private fun openGeneratedPDF(){
        val directory = File(Environment.getExternalStorageDirectory().absolutePath + "/medicalCertificate.pdf")
        if(directory.exists()){
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.fromFile(directory)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            }
            catch(e: Exception){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }//openGeneratedPDF function
}
