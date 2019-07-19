package com.origami.mdrecord.adapters

import com.origami.mdrecord.R
import com.origami.mdrecord.objects.PatientMedicationObject
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.patient_medication_layout.view.*

class MedicationRow(val patientMedicationObject: PatientMedicationObject) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.drug_name_textview_patient_medication.text = patientMedicationObject.drugName
        viewHolder.itemView.dose_textview_patient_medication.text = patientMedicationObject.dose
        viewHolder.itemView.frequency_textview_patient_medication.text = patientMedicationObject.frequency
        viewHolder.itemView.instructions_textview_patient_medication.text = patientMedicationObject.instructions
    }

    override fun getLayout(): Int {
        return R.layout.patient_medication_layout
    }
}