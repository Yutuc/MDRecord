package com.origami.mdrecord.patientfile


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity

import com.origami.mdrecord.R
import com.origami.mdrecord.adapters.MedicineRow
import com.origami.mdrecord.objects.MedicineObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.confirm_delete_alert_dialog.view.*
import kotlinx.android.synthetic.main.edit_or_delete_alert_dialog.view.*
import kotlinx.android.synthetic.main.fragment_medicine.view.*

class MedicineFragment : Fragment() {

    companion object{
        var medicineClicked: MedicineRow? = null
    }

    val medicineArrayList = ArrayList<MedicineObject>()
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

        pullMedicine()
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

    private fun pullMedicine(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}/medicineArrayList")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val medicineObject = p0.getValue(MedicineObject::class.java)!!
                medicineArrayList.add(medicineObject)
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }//pullMedicine functions

    private fun refreshRecyclerView(){
        adapter.clear()
        medicineArrayList.forEach {
            adapter.add(MedicineRow(it))
        }
    }//refreshRecyclerView function
}
