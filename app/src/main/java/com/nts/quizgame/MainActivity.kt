package com.nts.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.nts.quizgame.databinding.ActivityMainBinding
import com.nts.quizgame.databinding.ActivityWelcomeMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.buttonSignOut.setOnClickListener {

            //Firebasse Email and Password
            FirebaseAuth.getInstance().signOut()

            //Google account
            val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInAccount=GoogleSignIn.getClient(this,gso)
            googleSignInAccount.signOut().addOnCompleteListener {task ->

            if (task.isSuccessful){

                Toast.makeText(this
                ,"User Sign Out Successful"
                ,Toast.LENGTH_SHORT).show()


            }else{
                Toast.makeText(this
                    ,task.exception?.localizedMessage
                    ,Toast.LENGTH_SHORT).show()
            }

            }

            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        mainBinding.buttonStartQuiz.setOnClickListener {
            val intent=Intent(this,QuizActivity::class.java)
            startActivity(intent)
        }
    }
}