package com.nts.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nts.quizgame.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding=ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)

        signUpBinding.buttonSignUp.setOnClickListener {

            val email = signUpBinding.editTextSignUpEmail.text.toString()
            val password = signUpBinding.editTextSignUpPassword.text.toString()

            signupWithFirebase(email, password)

        }
    }

    fun signupWithFirebase(email:String,password:String){

        //To used Firebase Authentication we need to create object of firebase AUTH
        //Used Code Assistance Menu Android Studio For Authentication
        signUpBinding.progressBarSignUp.visibility=View.VISIBLE
        signUpBinding.buttonSignUp.isClickable=false

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->

            if(task.isSuccessful){

                Toast.makeText(this,"User Acoocunt Has Been Created"
                    ,Toast.LENGTH_SHORT).show()
                finish()
                signUpBinding.progressBarSignUp.visibility=View.INVISIBLE
                signUpBinding.buttonSignUp.isClickable=true
            }
            else{
                Toast.makeText(this
                    , task.exception?.localizedMessage
                    ,Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}