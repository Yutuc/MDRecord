package com.origami.mdrecord.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val lastName = last_name_input_register.text.toString().trim()
        val email = email_input_register.text.toString().trim()
        val password = password_input_register.text.toString().trim()
        val confirmPassword = confirm_password_input_register.text.toString().trim()

        if(firstName.isEmpty()){
            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(lastName.isEmpty()){
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show()
            return
        }
        else if(email.isEmpty()){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
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
                    saveUserToFirebase(firstName, lastName, email, password)
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }//registerUser function

    private fun saveUserToFirebase(firstName: String, lastName: String, email: String, password: String){
        val uid = FirebaseAuth.getInstance().uid
        val user = UserObject(uid!!, firstName, lastName, email, password)
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Successful registration", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ChoosePatientActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //clears the stack of activities
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }//saveUserToFirebase function
}
