package com.origami.mdrecord.adapters

import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicalAbstractFormObject
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.medical_abstract_form_layout.view.*

class MedicalAbstractFormRow(val medicalAbstractFormObject: MedicalAbstractFormObject) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.date_textview_medical_abstract_form_layout.text = medicalAbstractFormObject.date
        viewHolder.itemView.medical_history_textview_textview_medical_abstract_form_layout.text = medicalAbstractFormObject.medicalHistory
        viewHolder.itemView.physical_examination_textview_medical_abstract_form_layout.text = medicalAbstractFormObject.phyisicalExamination
        viewHolder.itemView.diagnoses_textview_medical_abstract_form_layout.text = medicalAbstractFormObject.diagnoses
        viewHolder.itemView.plans_textview_medical_abstract_form_layout.text = medicalAbstractFormObject.plans
    }

    override fun getLayout(): Int {
        return R.layout.medical_abstract_form_layout
    }
}