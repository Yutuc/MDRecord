package com.origami.mdrecord

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.objects.UserObject
import com.origami.mdrecord.patientfile.ViewPatientFileActivity
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.change_password_alert_dialog.view.*

class EditUserActivity : AppCompatActivity() {

    val currentUser = ViewPatientFileActivity.currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        setTitle("Edit ${currentUser.firstName} ${currentUser.middleName.get(0)}. ${currentUser.lastName}")

        change_password_textview_edit_user.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.change_password_alert_dialog, null)
            dialogBuilder.setView(dialogView)

            val alertDialog = dialogBuilder.create()!!
            alertDialog.setTitle("Change password")
            alertDialog.show()

            dialogView.confirm_change_password_button_change_password_alert_dialog.setOnClickListener {
                val oldPassword = dialogView.old_password_input_change_password_input_alert_dialog.text.toString().trim()
                val newPassword = dialogView.new_password_change_password_input_alert_dialog.text.toString().trim()
                val newPasswordConfirm = dialogView.confirm_new_password_change_password_input_alert_dialog.text.toString().trim()

                if(oldPassword.isEmpty()){
                    Toast.makeText(this, "Please enter your old password", Toast.LENGTH_SHORT).show()
                }
                else if(newPassword.isEmpty()){
                    Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show()
                }
                else if(newPasswordConfirm.isEmpty()){
                    Toast.makeText(this, "Please confirm your new password", Toast.LENGTH_SHORT).show()
                }
                else if(currentUser.password != oldPassword){
                    Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show()
                }
                else if(newPassword != newPasswordConfirm){
                    Toast.makeText(this, "New password and confirmation password don't match", Toast.LENGTH_SHORT).show()
                }
                else{
                    val user = FirebaseAuth.getInstance().currentUser
                    val credentials = EmailAuthProvider.getCredential(user!!.email!!, oldPassword)
                    user.reauthenticate(credentials).addOnCompleteListener {
                        if(it.isSuccessful){
                            user.updatePassword(newPassword).addOnCompleteListener {
                                if(it.isSuccessful){
                                    val ref = FirebaseDatabase.getInstance().getReference("users/${currentUser.uid}").child("password")
                                    ref.setValue(newPassword)
                                    Toast.makeText(this, "Successfully changed password", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                }
                                else{
                                    Toast.makeText(this, "Error: Couldn't update password at this time", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            Toast.makeText(this, "Error: Couldn't update password at this time", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        displayUserInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_user_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_edit_user -> {
                val firstName = first_name_input_edit_user.text.toString().trim()
                val middleName = middle_name_input_edit_user.text.toString().trim()
                val lastName = last_name_input_edit_user.text.toString().trim()
                val specialty = specialty_input_edit_user.text.toString().trim()
                val licenseNumber = license_number_input_edit_user.text.toString().trim()
                val insuranceProviderNumber = insurance_provider_number_edit_user.text.toString().trim()
                val s2Number = s2_number_input_edit_user.text.toString().trim()
                val clinicAddress = clinic_address_input_edit_user.text.toString().trim()
                val contactNumber = contact_number_input_edit_user.text.toString().trim()
                val email = email_input_edit_user.text.toString().trim()
                val password = password_input_edit_user.text.toString().trim()

                if(firstName.isEmpty()){
                    Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(middleName.isEmpty()){
                    Toast.makeText(this, "Please enter your middle name", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(lastName.isEmpty()){
                    Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(specialty.isEmpty()){
                    Toast.makeText(this, "Please enter your specialty", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(licenseNumber.isEmpty()){
                    Toast.makeText(this, "Please enter your license number", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(insuranceProviderNumber.isEmpty()){
                    Toast.makeText(this, "Please enter your insurance provider number", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(s2Number.isEmpty()){
                    Toast.makeText(this, "Please enter your S2 number", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(clinicAddress.isEmpty()){
                    Toast.makeText(this, "Please enter your clinic address", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(contactNumber.isEmpty()){
                    Toast.makeText(this, "Please enter your contact number", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(email.isEmpty()){
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(password.isEmpty()){
                    Toast.makeText(this, "Please enter your password to save changes", Toast.LENGTH_SHORT).show()
                    return true
                }
                else if(password != currentUser.password){
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    return true
                }
                else{
                    if(currentUser.email != email){
                        val user = FirebaseAuth.getInstance().currentUser
                        val credentials = EmailAuthProvider.getCredential(user!!.email!!, password)
                        user.reauthenticate(credentials).addOnCompleteListener {
                            if(it.isSuccessful){
                                user.updateEmail(email).addOnCompleteListener {
                                    if(it.isSuccessful){
                                        saveUserToFirebase(firstName, middleName, lastName, specialty, licenseNumber, insuranceProviderNumber, s2Number, clinicAddress, contactNumber, email, password)
                                        Toast.makeText(this, "Successfully changed email", Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        Toast.makeText(this, "Error: Couldn't update email at this time", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            else{
                                Toast.makeText(this, "Error: Couldn't update email at this time", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        saveUserToFirebase(firstName, middleName, lastName, specialty, licenseNumber, insuranceProviderNumber, s2Number, clinicAddress, contactNumber, email, password)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveUserToFirebase(firstName: String, middleName: String, lastName: String, specialty:String, licenseNumber: String, insuranceProviderNumber: String, s2Number: String, clinicAddress: String, contactNumber: String, email: String, password: String){
        val uid = FirebaseAuth.getInstance().uid
        val user = UserObject(uid!!, firstName, middleName, lastName, specialty, licenseNumber, insuranceProviderNumber, s2Number, clinicAddress, contactNumber, email, password)
        val ref = FirebaseDatabase.getInstance().getReference("/users/${currentUser.uid}")

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully edited user", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }//saveUserToFirebase function

    private fun displayUserInfo(){
        first_name_input_edit_user.setText(currentUser.firstName)
        middle_name_input_edit_user.setText(currentUser.middleName)
        last_name_input_edit_user.setText(currentUser.lastName)
        specialty_input_edit_user.setText(currentUser.specialty)
        license_number_input_edit_user.setText(currentUser.licenseNumber)
        insurance_provider_number_edit_user.setText(currentUser.insuranceProviderNumber)
        s2_number_input_edit_user.setText(currentUser.s2Number)
        clinic_address_input_edit_user.setText(currentUser.clinicAddress)
        contact_number_input_edit_user.setText(currentUser.contactNumber)
        email_input_edit_user.setText(currentUser.email)
    }//displayUserInfo function
}
