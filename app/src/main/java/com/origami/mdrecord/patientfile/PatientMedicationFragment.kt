package com.origami.mdrecord.patientfile


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.origami.mdrecord.ChoosePatientActivity

import com.origami.mdrecord.R
import com.origami.mdrecord.adapters.MedicationRow
import com.origami.mdrecord.objects.PatientMedicationObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.confirm_patient_medication_delete_alert_dialog.view.*
import kotlinx.android.synthetic.main.edit_or_delete_patient_medication_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_patient_medication.view.*

class PatientMedicationFragment : Fragment() {

    companion object{
        var patientMedicationClicked: MedicationRow? = null
    }

    var patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    var patientMedicationArrayList = ArrayList<PatientMedicationObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_patient_medication, container, false)
        view.recyclerview_patient_medication.adapter = adapter

        adapter.setOnItemLongClickListener { item, _ ->
            patientMedicationClicked = item as MedicationRow
            val dialogBuilder = AlertDialog.Builder(context)
            var dialogView = layoutInflater.inflate(R.layout.edit_or_delete_patient_medication_alert_dialog, null)
            dialogBuilder.setView(dialogView)

            val editOrDeleteAlertDialog = dialogBuilder.create()!!
            editOrDeleteAlertDialog.show()

            dialogView.edit_button_edit_or_delete_alert_dialog.setOnClickListener {
                val intent = Intent(context, EditPatientMedicationActivity::class.java)
                startActivity(intent)
                editOrDeleteAlertDialog.dismiss()
            }

            dialogView.delete_button_edit_or_delete_alert_dialog.setOnClickListener {
                editOrDeleteAlertDialog.dismiss()

                dialogView = layoutInflater.inflate(R.layout.confirm_patient_medication_delete_alert_dialog, null)
                dialogBuilder.setView(dialogView)

                val deleteAlertDialog = dialogBuilder.create()!!
                deleteAlertDialog.show()

                dialogView.confirm_delete_button_confirm_patient_medication_delete_alert_dialog.setOnClickListener {
                    val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}/patient-medication").child("${patientMedicationClicked!!.patientMedicationObject.uid}")
                    ref.removeValue()
                    refreshRecyclerView()
                    deleteAlertDialog.dismiss()
                }
            }
            true
        }

        pullPatientInfo()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.patient_medication_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun pullPatientInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${patientObject.uid}/patient-medication")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val patientMedicationObject = p0.getValue(PatientMedicationObject::class.java)!!
                patientMedicationArrayList.set(find(patientMedicationObject.uid), patientMedicationObject)
                refreshRecyclerView()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val patientMedicationObject = p0.getValue(PatientMedicationObject::class.java)!!
                patientMedicationArrayList.add(patientMedicationObject)
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val patientMedicationObject = p0.getValue(PatientMedicationObject::class.java)!!
                patientMedicationArrayList.removeAt(find(patientMedicationObject.uid))
                refreshRecyclerView()
            }

        })
    }//pullPatientInfo function

    private fun find(editedPatientMedicationObjectUid: String) : Int{
        patientMedicationArrayList.forEachIndexed{ index, it ->
            if(it.uid == editedPatientMedicationObjectUid){
                return index
            }
        }
        return 0
    }//find function

    private fun refreshRecyclerView(){
        adapter.clear()
        patientMedicationArrayList.forEach {
            adapter.add(MedicationRow(it))
        }
    }//refreshRecyclerView function
}
