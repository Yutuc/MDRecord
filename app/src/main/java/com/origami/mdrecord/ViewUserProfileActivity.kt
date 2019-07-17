package com.origami.mdrecord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.origami.mdrecord.objects.UserObject
import com.origami.mdrecord.patientfile.ViewPatientFileActivity
import kotlinx.android.synthetic.main.activity_view_user_profile.*

class ViewUserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_profile)
        setTitle("Your profile")
        pullUserInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_user_profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_profile -> {
                val intent = Intent(this, EditUserActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullUserInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("/users").child("${FirebaseAuth.getInstance().uid}")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val currentUserPulled = p0.getValue(UserObject::class.java)
                ViewPatientFileActivity.currentUser = currentUserPulled
                displayUserInfo(currentUserPulled!!)
            }
        })
    }//pullUserInfo function

    private fun displayUserInfo(currentUser: UserObject){
        name_textview_view_user_profile.text = "${currentUser.firstName} ${currentUser.middleName.get(0)}. ${currentUser.lastName}, MD"
        specialty_textview_view_user_profile.text = currentUser.specialty
        license_number_textview_view_user_profile.text = "License number: ${currentUser.licenseNumber}"
        insurance_provider_number_textview_view_user_profile.text = "Insurance provider number: ${currentUser.insuranceProviderNumber}"
        s2_number_textview_view_user_profile.text = "S2 number: ${currentUser.s2Number}"
        clinic_address_textview_view_user_profile.text = "Clinic address: ${currentUser.clinicAddress}"
        contact_number_textview_view_user_profile.text = "Contact number: ${currentUser.contactNumber}"
        email_textview_view_user_profile.text = "Email: ${currentUser.email}"
    }//displayUserInfo functino
}
