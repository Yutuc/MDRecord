package com.origami.mdrecord.patientfile


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.adapters.AssessmentRow
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.AssessmentObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_medical_history.view.recyclerview_medical_history

class MedicalHistoryFragment : Fragment() {

    val medicalHistoryArrayList = ArrayList<AssessmentObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_medical_history, container, false)
        view.recyclerview_medical_history.adapter = adapter
        pullMedicalHistory()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.medical_history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun pullMedicalHistory(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}/medicalHistoryArrayList")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val assessmentObject = p0.getValue(AssessmentObject::class.java)!!
                medicalHistoryArrayList.add(assessmentObject)
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }//pullMedicalHistory function

    private fun refreshRecyclerView(){
        adapter.clear()
        medicalHistoryArrayList.forEach {
            adapter.add(AssessmentRow(it))
        }
        view?.recyclerview_medical_history?.layoutManager?.scrollToPosition(adapter.itemCount-1)
    }//refreshRecyclerView function
}
