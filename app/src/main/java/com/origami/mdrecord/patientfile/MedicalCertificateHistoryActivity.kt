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
import com.origami.mdrecord.adapters.MedicalCertificateRow
import com.origami.mdrecord.objects.MedicalCertificateObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_medical_certificate_history.*

class MedicalCertificateHistoryActivity : AppCompatActivity() {

    companion object{
        var medicalCertificateClicked: MedicalCertificateRow? = null
    }

    val patientObject = ChoosePatientActivity.patientClicked!!.patientObject
    val adapter = GroupAdapter<ViewHolder>()
    val medicalCertificateHistoryArrayList = ArrayList<MedicalCertificateObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_certificate_history)
        setTitle("Medical Certificate History")
        recyclerview_medical_certificate_history.adapter = adapter

        adapter.setOnItemLongClickListener { item, _ ->
            medicalCertificateClicked = item as MedicalCertificateRow
            val intent = Intent(this, ExpandedMedicalCertificateActivity::class.java)
            startActivity(intent)
            true
        }
        pullMedicalCertificates()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.medical_certificate_history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.create_medical_certificate -> {
                val intent = Intent(this, CreateMedicalCertificateActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullMedicalCertificates(){
        val currentUser = ViewPatientFileActivity.currentUser!!
        val currentPatient = ChoosePatientActivity.patientClicked!!.patientObject
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${currentUser.uid}/${currentPatient.uid}/medical-certificate-history")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val medicalCertificateObject = p0.getValue(MedicalCertificateObject::class.java)
                medicalCertificateHistoryArrayList.add(medicalCertificateObject!!)
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }//pullMedicalCertificates function

    private fun refreshRecyclerView(){
        adapter.clear()
        medicalCertificateHistoryArrayList.forEach {
            adapter.add(MedicalCertificateRow(it))
        }
    }//refreshRecyclerView function
}
