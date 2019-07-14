package com.origami.mdrecord.patientfile

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.AssessmentObject
import kotlinx.android.synthetic.main.activity_add_medical_assessment.*
import kotlinx.android.synthetic.main.confirm_assessment_alert_dialog.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.ArrayList

class AddMedicalAssessmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medical_assessment)
        setTitle("Add Medical Assessment")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_medical_assessment_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_medical_assessment -> {
                val assessment = assessment_input_medical_history.text.toString().trim()
                if(assessment.isEmpty()){
                    Toast.makeText(this, "Please enter an assessment", Toast.LENGTH_SHORT).show()
                    return true
                }
                else {
                    val dialogBuilder = AlertDialog.Builder(this)
                    val dialogView = layoutInflater.inflate(R.layout.confirm_assessment_alert_dialog, null)
                    dialogBuilder.setView(dialogView)

                    dialogView.confirm_message_textview_asessment_alert_dialog.text = "Once confirmed, you can't edit or delete an entry"

                    val alertDialog = dialogBuilder.create()!!
                    alertDialog.show()

                    dialogView.confirm_confirm_button_asessment_alert_dialog.setOnClickListener {
                        saveMedicalAssessment()
                        alertDialog.dismiss()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMedicalAssessment(){
        val timeStamp = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val dateAndTime = timeStamp.format(timeFormatter)
        val assessment = assessment_input_medical_history.text.toString().trim()

        var medicalHistoryArrayList = ChoosePatientActivity.patientClicked!!.patientObject.medicalHistoryArrayList

        if(medicalHistoryArrayList == null){
            medicalHistoryArrayList = ArrayList<AssessmentObject>()
        }
        ChoosePatientActivity.patientClicked!!.patientObject.medicalHistoryArrayList = medicalHistoryArrayList
        medicalHistoryArrayList.add(AssessmentObject(dateAndTime, assessment))

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("medicalHistoryArrayList")
        ref.setValue(medicalHistoryArrayList)

        finish()
    }//saveMedicalAssessment function
}
