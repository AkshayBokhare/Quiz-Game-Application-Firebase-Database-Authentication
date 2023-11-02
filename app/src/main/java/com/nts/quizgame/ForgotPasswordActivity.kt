package com.nts.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.nts.quizgame.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding
    val auth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotPasswordBinding.root
        setContentView(view)

        forgotPasswordBinding.buttonReset.setOnClickListener {

            val userEmail=forgotPasswordBinding.editTextForgotEmail.text.toString()
            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task->

                if (task.isSuccessful){
                    Toast.makeText(this,"We Send Password Reset Link To Your Register Email Address"
                        , Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this,task.exception?.localizedMessage
                        , Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}