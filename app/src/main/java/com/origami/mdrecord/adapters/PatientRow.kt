package com.origami.mdrecord.adapters

import com.origami.mdrecord.objects.PatientObject
import com.origami.mdrecord.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.patient_row.view.*

class PatientRow(var patientObject: PatientObject) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.patient_name_textview_patient_row.text = "${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName}"
    }

    override fun getLayout(): Int {
        return R.layout.patient_row
    }
}