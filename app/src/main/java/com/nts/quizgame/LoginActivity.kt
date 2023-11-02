package com.nts.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.nts.quizgame.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var googlSignInClient: GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)


        //Change Google Button
        val textGoogleButton = loginBinding.buttonGoogleSignIn.getChildAt(0) as TextView
        textGoogleButton.text = "Continue WIth Google"
        textGoogleButton.setTextColor(Color.BLACK)
        textGoogleButton.textSize = 16F

        //Register
        registerActivityForGoogleSignIn()

        loginBinding.buttonSignIn.setOnClickListener {

            val userEmail = loginBinding.editTextLoginEmail.text.toString()
            val userPassword = loginBinding.editTextLoginPassword.text.toString()

            signInUser(userEmail, userPassword)

        }

        loginBinding.buttonGoogleSignIn.setOnClickListener {

            signInGoogle()

        }

        loginBinding.textViewSignUp.setOnClickListener {

            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)

        }

        loginBinding.textViewForgotPassword.setOnClickListener {

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)

        }
    }


    fun signInUser(userEmail: String, userPassword: String) {

        //To used Firebase Authentication we need to create object of firebase AUTH
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                Toast.makeText(
                    this, "Welcome To Quiz Game", Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this, task.exception?.localizedMessage, Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    // Stay Login User To application
    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            Toast.makeText(
                this, "Welcome To Quiz Game", Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("675189544807-rog42ppkq4oe6841s2ht639nu3l9jbvf.apps.googleusercontent.com")
            .requestEmail().build()

        googlSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private fun signIn() {
        val signInIntent: Intent = googlSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun registerActivityForGoogleSignIn() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->

                    val resultCode = result.resultCode
                    val data = result.data

                    if (resultCode == RESULT_OK && data != null) {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(data)
                        firebaseSignInWithGoogle(task)
                    }


                })

    }


    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(
                applicationContext, "Welcome to Quiz Game", Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

            firebaseGoogleAccount(account)
        } catch (e: ApiException) {
            Toast.makeText(
                applicationContext, e.localizedMessage, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->

            if (task.isSuccessful) {

//                val user =auth.currentUser
//                user.
                //here we can get users Name ,phonr number ,img,email etc.

            } else {


            }
        }
    }


}