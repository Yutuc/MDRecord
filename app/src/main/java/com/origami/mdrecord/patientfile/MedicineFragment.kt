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
import com.origami.mdrecord.adapters.MedicineRow
import com.origami.mdrecord.objects.MedicineObject
import com.origami.mdrecord.objects.PatientObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.confirm_delete_alert_dialog.view.*
import kotlinx.android.synthetic.main.edit_or_delete_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_medicine.view.*

class MedicineFragment : Fragment() {

    companion object{
        var medicineClicked: MedicineRow? = null
    }

    var patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    var medicineArrayList = ArrayList<MedicineObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_medicine, container, false)
        view.recyclerview_medicine.adapter = adapter

        adapter.setOnItemLongClickListener { item, _ ->
            medicineClicked = item as MedicineRow
            val dialogBuilder = AlertDialog.Builder(context)
            var dialogView = layoutInflater.inflate(R.layout.edit_or_delete_alert_dialog, null)
            dialogBuilder.setView(dialogView)

            val alertDialog = dialogBuilder.create()!!
            alertDialog.show()

            dialogView.edit_button_edit_or_delete_alert_dialog.setOnClickListener {
                val intent = Intent(context, EditMedicineActivity::class.java)
                startActivity(intent)
                alertDialog.dismiss()
            }

            dialogView.delete_button_edit_or_delete_alert_dialog.setOnClickListener {
                alertDialog.dismiss()

                dialogView = layoutInflater.inflate(R.layout.confirm_delete_alert_dialog, null)
                dialogBuilder.setView(dialogView)

                val alertDialog = dialogBuilder.create()!!
                alertDialog.show()

                dialogView.confirm_delete_button_confirm_delete_alert_dialog.setOnClickListener {
                    medicineArrayList.remove(medicineClicked!!.medicineObject)
                    ChoosePatientActivity.patientClicked!!.patientObject.medicineArrayList = medicineArrayList
                    val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}").child("medicineArrayList")
                    ref.setValue(medicineArrayList)
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
        inflater?.inflate(R.menu.medicine_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun pullPatientInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}").child("${patientObject.uid}")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val patientObject = p0.getValue(PatientObject::class.java)
                medicineArrayList = patientObject!!.medicineArrayList!!
                refreshRecyclerView()
            }

        })
    }//pullPatientInfo function

    private fun refreshRecyclerView(){
        adapter.clear()
        medicineArrayList.forEach {
            adapter.add(MedicineRow(it))
        }
    }//refreshRecyclerView function
}
