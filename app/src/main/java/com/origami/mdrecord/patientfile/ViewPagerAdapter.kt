package com.origami.mdrecord.patientfile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter (fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                MedicalHistoryFragment()
            }
            1 -> {
                MedicationFragment()
            }
            2 -> {
                LabHistoryFragment()
            }
            else -> {
                NotesFragment()
            }
        }
    }

    override fun getCount(): Int {
            return 4 //number of fragments
    }

    /*override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> {
                "Medical History"
            }
            1 -> {
                "Medicine"
            }
            2 -> {
               "Lab History"
            }
            else -> {
                "Notes"
            }
        }
    }*/
}