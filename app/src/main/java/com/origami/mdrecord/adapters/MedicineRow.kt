package com.origami.mdrecord.adapters

import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicineObject
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.medicine_layout.view.*

class MedicineRow(val medicineObject: MedicineObject) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.drug_name_textview_medicine.text = medicineObject.drugName
        viewHolder.itemView.dose_textview_medicine.text = medicineObject.dose
        viewHolder.itemView.frequency_textview_medicine.text = medicineObject.frequency
        viewHolder.itemView.instructions_textview_medicine.text = medicineObject.instructions
    }

    override fun getLayout(): Int {
        return R.layout.medicine_layout
    }
}