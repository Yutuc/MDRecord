package com.origami.mdrecord.adapters

import com.origami.mdrecord.R
import com.origami.mdrecord.objects.AssessmentObject
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.assessment_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class AssessmentRow(val assessmentObject: AssessmentObject) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.date_and_time_textview_assessment.text = assessmentObject.dateAndTime
        viewHolder.itemView.assessment_textview_assessment.text = assessmentObject.assessment
    }

    override fun getLayout(): Int {
        return R.layout.assessment_layout
    }
}