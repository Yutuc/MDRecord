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
import com.origami.mdrecord.objects.MedicationObject
import com.origami.mdrecord.objects.PatientObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.confirm_medication_delete_alert_dialog.view.*
import kotlinx.android.synthetic.main.edit_or_delete_medication_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_medication.view.*

class MedicationFragment : Fragment() {

    companion object{
        var medicationClicked: MedicationRow? = null
    }

    var patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    var medicationArrayList = ArrayList<MedicationObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_medication, container, false)
        view.recyclerview_medicine.adapter = adapter

        adapter.setOnItemLongClickListener { item, _ ->
            medicationClicked = item as MedicationRow
            val dialogBuilder = AlertDialog.Builder(context)
            var dialogView = layoutInflater.inflate(R.layout.edit_or_delete_medication_alert_dialog, null)
            dialogBuilder.setView(dialogView)

            val alertDialog = dialogBuilder.create()!!
            alertDialog.show()

            dialogView.edit_button_edit_or_delete_alert_dialog.setOnClickListener {
                val intent = Intent(context, EditMedicationActivity::class.java)
                startActivity(intent)
                alertDialog.dismiss()
            }

            dialogView.delete_button_edit_or_delete_alert_dialog.setOnClickListener {
                alertDialog.dismiss()

                dialogView = layoutInflater.inflate(R.layout.confirm_medication_delete_alert_dialog, null)
                dialogBuilder.setView(dialogView)

                val alertDialog = dialogBuilder.create()!!
                alertDialog.show()

                dialogView.confirm_delete_button_confirm_medicine_delete_alert_dialog.setOnClickListener {
                    medicationArrayList.remove(medicationClicked!!.medicationObject)
                    ChoosePatientActivity.patientClicked!!.patientObject.medicationArrayList = medicationArrayList
                    val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("medicationArrayList")
                    ref.setValue(medicationArrayList)
                    refreshRecyclerView()
                    alertDialog.dismiss()
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
        inflater?.inflate(R.menu.medication_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun pullPatientInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}").child("${patientObject.uid}")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val patientObject = p0.getValue(PatientObject::class.java)
                medicationArrayList = patientObject!!.medicationArrayList!!
                refreshRecyclerView()
            }

        })
    }//pullPatientInfo function

    private fun refreshRecyclerView(){
        adapter.clear()
        medicationArrayList.forEach {
            adapter.add(MedicationRow(it))
        }
    }//refreshRecyclerView function
}
