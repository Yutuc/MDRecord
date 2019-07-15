package com.origami.mdrecord.patientfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import kotlinx.android.synthetic.main.activity_view_patient_file.*
import java.util.*

class ViewPatientFileActivity : AppCompatActivity() {

    companion object{
        var currentTab = 0
    }

    var patientObject = ChoosePatientActivity.patientClicked!!.patientObject

    val tabIconsArrayList = arrayOf(
        R.drawable.ic_medical_history_white,
        R.drawable.ic_medicine_white,
        R.drawable.ic_labs_white,
        R.drawable.ic_notes_white
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_patient_file)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_drawer_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.create_medical_certificate ->{

                }
                R.id.create_medical_abstract_form ->{

                }
            }
            drawer_layout.closeDrawer(GravityCompat.START)
            false //controls whether or not the nav menu item is kept selected on click
        }

        navigation_drawer_view.getHeaderView(0).setOnClickListener {
            /*val intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)*/
        }

        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager)

        view_pager_patient_file.adapter = fragmentAdapter
        view_pager_patient_file.offscreenPageLimit = fragmentAdapter!!.count
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
                currentTab = position
            }

        })

        val tab = tab_layout_patient_file.getTabAt(currentTab)
        tab!!.select()

        app_bar_patient_file.setOnClickListener {
            val intent = Intent(this, ExpandedPatientInfoActivity::class.java)
            startActivity(intent)
        }

        displayPatientInfo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.open_calendar -> {
                val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI)
                startActivity(intent)
            }
            R.id.add_medical_assessment -> {
                val intent = Intent(this, AddMedicalAssessmentActivity::class.java)
                startActivity(intent)
            }
            R.id.add_medicine -> {
                val intent = Intent(this, AddMedicineActivity::class.java)
                startActivity(intent)
            }
            R.id.add_lab -> {
                val intent = Intent(this, AddLabAssessmentActivity::class.java)
                startActivity(intent)
            }
            R.id.add_note -> {
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayPatientInfo(){
        name_age_gender_textview_patient_file.text = "${patientObject.firstName} ${patientObject.middleName} ${patientObject.lastName} ${getAge()}/${getGender()}"
        address_textview_patient_file.text = "Address: ${ChoosePatientActivity.patientClicked?.patientObject?.address}"
        contact_number_textview_patient_file.text = "Contact number: ${patientObject.phoneNumber}"
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
            val intent = Intent(this, ChoosePatientActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //clears the stack of activities
            startActivity(intent)
            //NavUtils.navigateUpFromSameTask(this) //makes back pressed go to parent activity
        }
    }
}
