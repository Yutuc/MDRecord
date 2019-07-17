package com.origami.mdrecord.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.origami.mdrecord.ChoosePatientActivity
import com.origami.mdrecord.R
import com.origami.mdrecord.objects.UserObject
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setTitle("Register")
        register_button_register.setOnClickListener {
            registerUser()
        }

        already_have_an_account_textview.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //clears the stack of activities
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(){
        val firstName = first_name_input_register.text.toString().trim()
        val middleName = middle_name_input_register.text.toString().trim()
        val lastName = last_name_input_register.text.toString().trim()
        val specialty = specialty_input_register.text.toString().trim()
        val licenseNumber = license_number_input_register.text.toString().trim()
        val insuranceProviderNumber = insurance_provider_number_register.text.toString().trim()
        val s2Number = s2_number_input_register.text.toString().trim()
        val clinicAddress = clinic_address_input_register.text.toString().trim()
        val contactNumber = contact_number_input_register.text.toString().trim()

        val email = email_input_register.text.toString().trim()
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val password = password_input_register.text.toString().trim()
        val confirmPassword = confirm_password_input_register.text.toString().trim()

        if(firstName.isEmpty()){
            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(middleName.isEmpty()){
            Toast.makeText(this, "Please enter your middle name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(lastName.isEmpty()){
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(specialty.isEmpty()){
            Toast.makeText(this, "Please enter your specialty", Toast.LENGTH_SHORT).show()
            return
        }
        else if(licenseNumber.isEmpty()){
            Toast.makeText(this, "Please enter your license number", Toast.LENGTH_SHORT).show()
            return
        }
        else if(insuranceProviderNumber.isEmpty()){
            Toast.makeText(this, "Please enter your insurance provider number", Toast.LENGTH_SHORT).show()
            return
        }
        else if(s2Number.isEmpty()){
            Toast.makeText(this, "Please enter your S2 number", Toast.LENGTH_SHORT).show()
            return
        }
        else if(clinicAddress.isEmpty()){
            Toast.makeText(this, "Please enter your clinic address", Toast.LENGTH_SHORT).show()
            return
        }
        else if(contactNumber.isEmpty()){
            Toast.makeText(this, "Please enter your contact number", Toast.LENGTH_SHORT).show()
            return
        }
        else if(email.isEmpty()){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }
        else if(!isEmailValid){
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }
        else if(password.isEmpty()){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }
        else if(confirmPassword.isEmpty()){
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
            return
        }
        else if(password != confirmPassword){
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                    saveUserToFirebase(firstName, middleName, lastName, specialty, licenseNumber, insuranceProviderNumber, s2Number, clinicAddress, contactNumber, email, password)
                    FirebaseAuth.getInstance().signOut()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }//registerUser function

    private fun saveUserToFirebase(firstName: String, middleName: String, lastName: String, specialty:String, licenseNumber: String, insuranceProviderNumber: String, s2Number: String, clinicAddress: String, contactNumber: String, email: String, password: String){
        val uid = FirebaseAuth.getInstance().uid
        val user = UserObject(uid!!, firstName, middleName, lastName, specialty, licenseNumber, insuranceProviderNumber, s2Number, clinicAddress, contactNumber, email, password)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Successful registration\nPlease verify your email address before logging in", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }//saveUserToFirebase function
}
