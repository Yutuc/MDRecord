package com.origami.mdrecord.adapters

import com.origami.mdrecord.R
import com.origami.mdrecord.objects.MedicalCertificateObject
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.medical_certificate_layout.view.*

class MedicalCertificateRow (val medicalCertificateObject: MedicalCertificateObject) : Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.date_textview_medical_certificate_layout.text = medicalCertificateObject.date
        viewHolder.itemView.name_of_addressee_textview_medical_certificate_layout.text = medicalCertificateObject.nameOfAddresee
        viewHolder.itemView.position_of_addressee_textview_medical_certificate_layout.text = medicalCertificateObject.positionOfAddresee
        viewHolder.itemView.diagnosis_textview_medical_certificate_layout.text = medicalCertificateObject.diagnosis
        viewHolder.itemView.remarks_textview_medical_certificate_layout.text = medicalCertificateObject.remarks
    }

    override fun getLayout(): Int {
        return R.layout.medical_certificate_layout
    }
}