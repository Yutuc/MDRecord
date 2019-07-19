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
import com.origami.mdrecord.R
import com.origami.mdrecord.adapters.AssessmentRow
import com.origami.mdrecord.objects.AssessmentObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_lab_history.view.*

class LabHistoryFragment : Fragment() {

    val labHistoryArrayList = ArrayList<AssessmentObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_lab_history, container, false)
        view.recyclerview_lab_history.adapter = adapter
        pullLabHistory()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.lab_history_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun pullLabHistory(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}/${ChoosePatientActivity.patientClicked?.patientObject?.uid}/lab-history")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val assessmentObject = p0.getValue(AssessmentObject::class.java)!!
                labHistoryArrayList.add(assessmentObject)
                refreshRecyclerView()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }//pullLabHistory function

    private fun refreshRecyclerView(){
        adapter.clear()
        labHistoryArrayList.forEach {
            adapter.add(AssessmentRow(it))
        }
        view?.recyclerview_lab_history?.layoutManager?.scrollToPosition(adapter.itemCount-1)
    }//refreshRecyclerView function
}
