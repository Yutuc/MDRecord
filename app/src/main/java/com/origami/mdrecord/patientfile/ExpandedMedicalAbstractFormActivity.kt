package com.origami.mdrecord.patientfile

import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.origami.mdrecord.R
import kotlinx.android.synthetic.main.activity_expanded_medical_abstract_form.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ExpandedMedicalAbstractFormActivity : AppCompatActivity() {

    val medicalAbstractFormClicked = MedicalAbstractFormHistoryActivity.medicalAbstractFormClicked!!.medicalAbstractFormObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_medical_abstract_form)
        setTitle("View Medical Abstract Form")
        displayInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.expanded_medical_abstract_form_menu, menu)
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
        patient_name_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.patientName
        patient_age_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.patientAge
        patient_gender_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.patientGender
        patient_address_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.patientAddress
        medical_history_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.medicalHistory
        physical_examination_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.phyisicalExamination
        diagnoses_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.diagnoses
        plans_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.plans
        name_of_doctor_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.doctorName
        license_number_textview_view_medical_abstract_form_pdf.text = medicalAbstractFormClicked.licenseNumber
    }//displayInfo function

    private fun createPDF(){
        val width = constraint_layout_scrollview_view_medical_abstract_form_pdf.measuredWidth
        val height = constraint_layout_scrollview_view_medical_abstract_form_pdf.measuredHeight
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()

        //creates a page
        val page = document.startPage(pageInfo)
        val content = constraint_layout_scrollview_view_medical_abstract_form_pdf
        content.draw(page.canvas)
        document.finishPage(page)

        //multiple pages can be created the same way here

        val path = Environment.getExternalStorageDirectory().absolutePath + "/medicalAbstractForm.pdf"
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
    }//viewPDF function

    private fun openGeneratedPDF(){
        val directory = File(Environment.getExternalStorageDirectory().absolutePath + "/medicalAbstractForm.pdf")
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
