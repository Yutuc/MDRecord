package com.origami.mdrecord.adapters

import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicationObject
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.medicine_layout.view.*

class MedicationRow(val medicationObject: MedicationObject) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.drug_name_textview_medicine.text = medicationObject.drugName
        viewHolder.itemView.dose_textview_medicine.text = medicationObject.dose
        viewHolder.itemView.frequency_textview_medicine.text = medicationObject.frequency
        viewHolder.itemView.instructions_textview_medicine.text = medicationObject.instructions
    }

    override fun getLayout(): Int {
        return R.layout.medicine_layout
    }
}