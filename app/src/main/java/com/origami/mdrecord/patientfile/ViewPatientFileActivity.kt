package com.origami.mdrecord.patientfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.PatientObject
import com.origami.mdrecord.objects.UserObject
import kotlinx.android.synthetic.main.activity_view_patient_file.*
import kotlinx.android.synthetic.main.navigation_drawer_header.view.*
import java.util.*

class ViewPatientFileActivity : AppCompatActivity() {

    companion object{
        var currentTab = 0
        var currentUser: UserObject? = null
    }

    var patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    val tabIconsArrayList = arrayOf(
        R.drawable.ic_medical_history_white,
        R.drawable.ic_pharmacy_white,
        R.drawable.ic_labs_white,
        R.drawable.ic_notes_white
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_patient_file)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Medical History")

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_drawer_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.medical_certificate_history -> {
                    val intent = Intent(this, MedicalCertificateHistoryActivity::class.java)
                    startActivity(intent)
                }
                R.id.create_medical_abstract_form -> {

                }
                R.id.schedule_appointment -> {
                    val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)
                    startActivity(intent)
                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            false //controls whether or not the nav menu item is kept selected on click
        }

        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager)

        view_pager_patient_file.adapter = fragmentAdapter
        view_pager_patient_file.offscreenPageLimit = fragmentAdapter.count
        tab_layout_patient_file.setupWithViewPager(view_pager_patient_file)
        for (x in 0 until tabIconsArrayList.size){
            tab_layout_patient_file.getTabAt(x)?.setIcon(tabIconsArrayList.get(x))
        }

        view_pager_patient_file.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 ->{
                        supportActionBar!!.setTitle("Medical History")
                    }
                    1 ->{
                        supportActionBar!!.setTitle("Patient's Medication")
                    }
                    2 -> {
                        supportActionBar!!.setTitle("Lab History")
                    }
                    else -> {
                        supportActionBar!!.setTitle("Notes")
                    }
                }
                currentTab = position
            }
        })

        val tab = tab_layout_patient_file.getTabAt(currentTab)
        tab!!.select()

        app_bar_patient_file.setOnClickListener {
            val intent = Intent(this, ExpandedPatientInfoActivity::class.java)
            startActivity(intent)
        }

        pullUserInfo()
        pullPatientInfo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_medical_assessment -> {
                val intent = Intent(this, AddMedicalAssessmentActivity::class.java)
                startActivity(intent)
            }
            R.id.add_patient_medication -> {
                val intent = Intent(this, AddPatientMedicationActivity::class.java)
                startActivity(intent)
            }
            R.id.add_lab_result -> {
                val intent = Intent(this, AddLabResultActivity::class.java)
                startActivity(intent)
            }
            R.id.add_note -> {
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullUserInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("/users").child("${FirebaseAuth.getInstance().uid}")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentUserPulled = p0.getValue(UserObject::class.java)
                currentUser = currentUserPulled
                displayUserInfoInNavigationHeader(currentUserPulled!!)
            }
        })
    }//pullUserInfo function

    private fun pullPatientInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("/patients/${FirebaseAuth.getInstance().uid}").child("${patientObject.uid}")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val patientObject = p0.getValue(PatientObject::class.java)
                displayPatientInfo(patientObject!!)
            }
        })
    }//pullPatientInfo function

    private fun displayUserInfoInNavigationHeader(currentUser: UserObject){
        val navigationHeader = navigation_drawer_view.getHeaderView(0)
        navigationHeader.name_textview_navigation_drawer_header.text = "${currentUser.firstName} ${currentUser.middleName.get(0)}. ${currentUser.lastName}, MD"
        navigationHeader.specialty_textview_navigation_drawer_header.text = currentUser.specialty
        navigationHeader.license_number_textview_navigation_drawer_header.text = "License #: ${currentUser.licenseNumber}"
        navigationHeader.insurance_provider_number_textview_navigation_drawer_header.text = "Insurance provider #: ${currentUser.insuranceProviderNumber}"
        navigationHeader.s2_number_textview_navigation_drawer_header.text = "S2 #: ${currentUser.s2Number}"
    }//displayUserInfoInNavigationHeader function

    private fun displayPatientInfo(patientObject: PatientObject){
        name_age_gender_textview_patient_file.text = "${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName} ${getAge()}/${getGender()}"
        address_textview_patient_file.text = "Address: ${ChoosePatientActivity.patientClicked?.patientObject?.address}"
        contact_number_textview_patient_file.text = "Contact number: ${patientObject.contactNumber}"
        email_textview_patient_file.text = "Email: ${patientObject.email}"
        if(ChoosePatientActivity.patientClicked?.patientObject?.diagnoses?.isEmpty()!!){
            diagnoses_textview_patient_file.text = "Diagnoses: N/A"
        }
        else{
            diagnoses_textview_patient_file.text = "Diagnoses: ${patientObject.diagnoses}"
        }
    }//displayPatientInfo function

    private fun getAge() : String{
        val dateOfBirthSplit = patientObject.dateOfBirth.split("/")
        val birthYear = dateOfBirthSplit.get(dateOfBirthSplit.size-1).toInt()
        val currentyear = Calendar.getInstance().get(Calendar.YEAR)
        return (currentyear-birthYear).toString()
    }//getAge function

    private fun getGender() : String{
        if(patientObject.gender == "Male"){
            return "M"
        }
        else{
            return "F"
        }
    }//getGender function

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
            //NavUtils.navigateUpFromSameTask(this) //makes back pressed go to parent activity
        }
    }
}
