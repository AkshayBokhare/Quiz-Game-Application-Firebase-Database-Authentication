package com.nts.quizgame


import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nts.quizgame.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    lateinit var quizBinding: ActivityQuizBinding

    //Create Databse object
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")


    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""

    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0

    lateinit var timer: CountDownTimer
    private val totalTime = 25000L
    var timerContinue = false
    var leftTime = totalTime

    //Storing score to databse
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreReference = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view = quizBinding.root
        setContentView(view)

        //Calling Functions
        gameLogic()

        quizBinding.buttonNext.setOnClickListener {
            resetTimer()
            gameLogic()

        }

        quizBinding.buttonFinish.setOnClickListener {

            sendScore()


        }

        quizBinding.textViewA.setOnClickListener {

            pauseTimer()
            userAnswer = "a"
            if (correctAnswer == userAnswer) {

                quizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()

            } else {
                quizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        quizBinding.textViewB.setOnClickListener {

            pauseTimer()
            userAnswer = "b"

            if (correctAnswer == userAnswer) {

                quizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()

            } else {
                quizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        quizBinding.textViewC.setOnClickListener {

            pauseTimer()
            userAnswer = "c"

            if (correctAnswer == userAnswer) {

                quizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()

            } else {
                quizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        quizBinding.textViewD.setOnClickListener {

            pauseTimer()

            userAnswer = "d"

            if (correctAnswer == userAnswer) {

                quizBinding.textViewD.setBackgroundColor(Color.GREEN)

                userCorrect++

                quizBinding.textViewCorrect.text = userCorrect.toString()

            } else {
                quizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                quizBinding.textViewWrong.text = userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

    }

    fun gameLogic() {

        restoreOption()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount) {

                    //Retrive Data From DataBase
                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()

                    correctAnswer =
                        snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.textViewQuestion.text = question
                    quizBinding.textViewA.text = answerA
                    quizBinding.textViewB.text = answerB
                    quizBinding.textViewC.text = answerC
                    quizBinding.textViewD.text = answerD

                    quizBinding.progressBarQuiz.visibility = View.INVISIBLE
                    quizBinding.linearLayoutInfo.visibility = View.VISIBLE
                    quizBinding.linearLayoutQuestion.visibility = View.VISIBLE
                    quizBinding.linearLayoutButtons.visibility = View.VISIBLE

                    startTimer()

                } else {

//                    Toast.makeText(
//                        applicationContext,
//                        "You Ans All MSG",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    val dialogMessage=AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulation!!!\nYou Answered All The Questions.Do You Want To See The Result ?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow,position ->

                      sendScore()

                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow,position ->

                       val intent =Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                    dialogMessage.create().show()

                }

                questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun findAnswer() {

        when (correctAnswer) {
            "a" -> quizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }

    }

    fun disableClickableOptions() {

        quizBinding.textViewA.isClickable = false
        quizBinding.textViewB.isClickable = false
        quizBinding.textViewC.isClickable = false
        quizBinding.textViewD.isClickable = false
    }

    fun restoreOption() {

        quizBinding.textViewA.setBackgroundColor(Color.WHITE)
        quizBinding.textViewB.setBackgroundColor(Color.WHITE)
        quizBinding.textViewC.setBackgroundColor(Color.WHITE)
        quizBinding.textViewD.setBackgroundColor(Color.WHITE)

        quizBinding.textViewA.isClickable = true
        quizBinding.textViewB.isClickable = true
        quizBinding.textViewC.isClickable = true
        quizBinding.textViewD.isClickable = true
    }

    fun startTimer() {

        timer = object : CountDownTimer(leftTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                leftTime = millisUntilFinished
                updateCountDownText()


            }

            override fun onFinish() {
                disableClickableOptions()
                resetTimer()
                updateCountDownText()
                quizBinding.textViewQuestion.text = "Sorry, Time is Up! Continue with next Question"
                timerContinue = false

            }


        }.start()
        timerContinue = true
    }

    fun updateCountDownText() {

        val remainingTime: Int = (leftTime / 1000).toInt()
        quizBinding.textViewTime.text = remainingTime.toString()


    }

    fun pauseTimer() {

        timer.cancel()
        timerContinue = false
    }

    fun resetTimer() {

        pauseTimer()
        leftTime = totalTime
        updateCountDownText()

    }

    fun sendScore(){
        user?.let {
            val userUID=it.uid

            scoreReference.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreReference.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {

                Toast.makeText(applicationContext
                ,"Score Store Into Database Successfully"
                ,Toast.LENGTH_SHORT).show()

                val intent= Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()

            }
        }

        }

}

