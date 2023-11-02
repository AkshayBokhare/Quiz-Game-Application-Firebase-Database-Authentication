package com.nts.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.window.SplashScreen
import com.nts.quizgame.databinding.ActivityWelcomeMainBinding

class WelcomeMainActivity : AppCompatActivity() {

    lateinit var splashBinding: ActivityWelcomeMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashBinding=ActivityWelcomeMainBinding.inflate(layoutInflater)
        val view = splashBinding.root
        setContentView(view)

        val alphaAnimation=AnimationUtils.loadAnimation(applicationContext,R.anim.splash_animation)
        splashBinding.textViewSplash.startAnimation(alphaAnimation)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object :Runnable{
            override fun run() {

                val intent = Intent(this@WelcomeMainActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        },3000)
    }
}