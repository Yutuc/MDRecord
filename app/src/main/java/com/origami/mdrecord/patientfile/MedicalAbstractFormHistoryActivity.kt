package com.origami.mdrecord.patientfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.adapters.MedicalAbstractFormRow
import com.origami.mdrecord.objects.MedicalAbstractFormObject
import com.origami.mdrecord.objects.MedicalCertificateObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_medical_abstract_form_history.*

class MedicalAbstractFormHistoryActivity : AppCompatActivity() {

    companion object{
        var medicalAbstractFormClicked: MedicalAbstractFormRow? = null
    }

    val medicalAbstractFormArrayList = ArrayList<MedicalAbstractFormObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_abstract_form_history)
        setTitle("Medical Abstract Form History")
        recyclerview_medical_abstract_form_history.adapter = adapter

        adapter.setOnItemLongClickListener { item, _ ->
            medicalAbstractFormClicked = item as MedicalAbstractFormRow
            val intent = Intent(this, ExpandedMedicalAbstractFormActivity::class.java)
            startActivity(intent)
            true
        }

        pullMedicalAbstractForms()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.medical_abstract_form_history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.create_medical_abstract_form -> {
                val intent = Intent(this, CreateMedicalAbstractFormActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullMedicalAbstractForms(){
        val currentUser = ViewPatientFileActivity.currentUser!!
        val currentPatient = ChoosePatientActivity.patientClicked!!.patientObject
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${currentUser.uid}/${currentPatient.uid}/medical-abstract-form-history")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val medicalAbstractFormObject = p0.getValue(MedicalAbstractFormObject::class.java)
                medicalAbstractFormArrayList.add(medicalAbstractFormObject!!)
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }//pullMedicalAbstractForms functions

    private fun refreshRecyclerView(){
        adapter.clear()
        medicalAbstractFormArrayList.forEach {
            adapter.add(MedicalAbstractFormRow(it))
        }
    }//refreshRecyclerView function
}
