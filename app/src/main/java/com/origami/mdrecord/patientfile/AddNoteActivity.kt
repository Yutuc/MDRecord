package com.origami.mdrecord.patientfile

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.AssessmentObject
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.confirm_assessment_alert_dialog.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.ArrayList

class AddNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setTitle("Add Note")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_note_assessment -> {
                val assessment = assessment_input_note.text.toString().trim()
                if(assessment.isEmpty()){
                    Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
                    return true
                }
                else{
                    val dialogBuilder = AlertDialog.Builder(this)
                    val dialogView = layoutInflater.inflate(R.layout.confirm_assessment_alert_dialog, null)
                    dialogBuilder.setView(dialogView)

                    dialogView.confirm_message_textview_asessment_alert_dialog.text = "Once confirmed, you can't edit or delete an entry"

                    val alertDialog = dialogBuilder.create()
                    alertDialog.show()

                    dialogView.confirm_confirm_button_asessment_alert_dialog.setOnClickListener {
                        saveNote()
                        alertDialog.dismiss()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote(){
        val timeStamp = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val dateAndTime = timeStamp.format(timeFormatter)
        val assessment = assessment_input_note.text.toString().trim()

        var notesArrayList = ChoosePatientActivity.patientClicked!!.patientObject.notesArrayList

        if(notesArrayList == null){
            notesArrayList = ArrayList<AssessmentObject>()
        }
        ChoosePatientActivity.patientClicked!!.patientObject.notesArrayList = notesArrayList
        notesArrayList!!.add(AssessmentObject(dateAndTime, assessment))

        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("notesArrayList")
        ref.setValue(notesArrayList)

        finish()
    }//saveNote function
}
