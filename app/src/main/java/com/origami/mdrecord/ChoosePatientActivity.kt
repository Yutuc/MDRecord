package com.origami.mdrecord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.adapters.PatientRow
import com.origami.mdrecord.objects.PatientObject
import com.origami.mdrecord.patientfile.ViewPatientFileActivity
import com.origami.mdrecord.registerlogin.LoginActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_choose_patient.*

class ChoosePatientActivity : AppCompatActivity() {

    companion object{
        var patientClicked: PatientRow? = null
    }

    val patientsMap = HashMap<String, PatientObject>()
    val patientsMapCopy = HashMap<String, PatientObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_patient)
        setTitle("Choose a patient")

        verifyUserIsLoggedIn()

        recyclerview_choose_patient.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerview_choose_patient.adapter = adapter

        adapter.setOnItemClickListener { item, _ ->
            patientClicked = item as PatientRow
            val intent = Intent(this, ViewPatientFileActivity::class.java)
            startActivity(intent)
        }

        pullPatients()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_patient_menu, menu)

        val searchIcon = menu?.findItem(R.id.search_patient)!!
        val searchView = searchIcon.actionView as SearchView
        val searchViewEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchViewEditText.hint = "Search patient"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    val search = newText.toLowerCase()
                    patientsMapCopy.clear()
                    patientsMap.values.forEach{
                        if(("${it.firstName} ${it.middleName} ${it.lastName}").toLowerCase().contains(search)){
                            patientsMapCopy[it.uid] = it
                        }
                    }
                    refreshRecyclerView(patientsMapCopy)
                }
                else{
                    refreshRecyclerView(patientsMap)
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search_patient-> {

            }

            R.id.add_patient -> {
                val intent = Intent(this, CreatePatientActivity::class.java)
                startActivity(intent)
            }

            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullPatients(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val patientObject = p0.getValue(PatientObject::class.java)!!
                patientsMap[p0.key!!] = patientObject
                patientsMapCopy[p0.key!!] = patientObject
                refreshRecyclerView(patientsMap)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val patientObject = p0.getValue(PatientObject::class.java)!!
                patientsMap[p0.key!!] = patientObject
                patientsMapCopy[p0.key!!] = patientObject
                refreshRecyclerView(patientsMap)
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }//pullPatients function

    private fun refreshRecyclerView(patientsMap: HashMap<String, PatientObject>){
        adapter.clear()
        patientsMap.values.forEach {
            adapter.add(PatientRow(it))
        }
    }//refreshRecyclerView function

    private fun verifyUserIsLoggedIn(){
        if(FirebaseAuth.getInstance().uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }//verifyUserIsLoggedIn function
}
