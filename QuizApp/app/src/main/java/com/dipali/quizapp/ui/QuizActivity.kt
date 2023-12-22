package com.dipali.quizapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dipali.quizapp.R
import com.dipali.quizapp.databinding.ActivityQuizBinding
import com.dipali.quizapp.ui.model.DatabaseHelper
import com.dipali.quizapp.ui.model.Question

object QuizManager {
    var score = 0
}

class QuizActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var timer: CountDownTimer

    lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DatabaseHelper(this)
        questions = dbHelper.getRandomQuestions(10)


        displayQuestion()

        startTimer()

        binding.finishButton.setOnClickListener {

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("TOTAL_SCORE", score)
            startActivity(intent)
            finish()
        }
    }

    fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }


    private fun startTimer() {
        timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.timerTextView.text = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                showNextQuestion()
            }
        }.start()
    }

    private fun stopTimer() {
        timer.cancel()
    }

    @SuppressLint("StringFormatInvalid")

    private fun displayQuestion() {
        if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
            val currentQuestion = questions[currentQuestionIndex]
            binding.questionTextView.text = currentQuestion.questionText
            binding.option1Button.text = currentQuestion.correctAnswer
            binding.option2Button.text = currentQuestion.incorrectAnswers[0]
            binding.option3Button.text = currentQuestion.incorrectAnswers[1]

            binding.option1Button.setOnClickListener { onAnswerSelected(binding.option1Button.text.toString()) }
            binding.option2Button.setOnClickListener { onAnswerSelected(binding.option2Button.text.toString()) }
            binding.option3Button.setOnClickListener { onAnswerSelected(binding.option3Button.text.toString()) }

            binding.totalScoreTextView.text =
                getString(R.string.score_placeholder, score.toString())
            binding.questionNumberTextView.text =
                getString(R.string.question_number_placeholder, currentQuestionIndex + 1)
        } else {

        }
    }


    @SuppressLint("StringFormatInvalid")
    private fun onAnswerSelected(selectedAnswer: String) {
        val currentQuestion = questions[currentQuestionIndex]
        val isCorrect = selectedAnswer == currentQuestion.correctAnswer

        score = if (isCorrect) {
            score + 1
        } else {
            score
        }

        binding.totalScoreTextView.text = getString(R.string.score_placeholder, score.toString())


        val correctButton = when {
            isCorrect -> binding.option1Button
            else -> when {
                selectedAnswer == currentQuestion.incorrectAnswers[0] -> binding.option2Button
                selectedAnswer == currentQuestion.incorrectAnswers[1] -> binding.option3Button
                else -> null
            }
        }

        correctButton?.setBackgroundColor(
            ContextCompat.getColor(
                this,
                if (isCorrect) R.color.green else R.color.red
            )
        )

        Handler(Looper.getMainLooper()).postDelayed({
            showNextQuestion()
        }, 500)
    }


    /* private fun onAnswerSelected(selectedAnswer: String) {

         val currentQuestion = questions[currentQuestionIndex]
         val isCorrect = selectedAnswer == currentQuestion.correctAnswer


         score += if (isCorrect) {
             val timeRemaining = binding.timerTextView.text?.toString()?.toLongOrNull() ?: 0
             val timeTaken = 20000 - timeRemaining
             (10 + timeTaken).coerceAtLeast(0).toInt()
         } else {
             0
         }

         val correctButton = when {
             isCorrect -> binding.option1Button
             else -> when {
                 selectedAnswer == currentQuestion.incorrectAnswers[0] -> binding.option2Button
                 selectedAnswer == currentQuestion.incorrectAnswers[1] -> binding.option3Button
                 else -> null
             }
         }

         correctButton?.setBackgroundColor(
             ContextCompat.getColor(
                 this,
                 if (isCorrect) R.color.green else R.color.red
             )
         )

         Handler(Looper.getMainLooper()).postDelayed({
             showNextQuestion()
         }, 500)

         *//*val button = when {
            isCorrect -> binding.option1Button
            isCorrect -> binding.option2Button
            isCorrect -> binding.option3Button

            else -> {
                binding.option1Button
                binding.option2Button
                binding.option3Button
            }
        }


        button.setBackgroundColor(
            ContextCompat.getColor(
                this,
                if (isCorrect) R.color.green else R.color.red
            )
        )
        Handler(Looper.getMainLooper()).postDelayed({
            showNextQuestion()
        },500)
*//*
        //showNextQuestion()
    }*/


    private fun showNextQuestion() {

        binding.option1Button.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        binding.option2Button.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        binding.option3Button.setBackgroundColor(resources.getColor(R.color.colorPrimary))

        if (::timer.isInitialized) {
            stopTimer()
        }


        currentQuestionIndex++

        if (currentQuestionIndex == questions.size) {

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("TOTAL_SCORE", score)
            startActivity(intent)
            finish()

        } else if (currentQuestionIndex < questions.size - 1) {

            //  binding.finishButton.visibility = View.VISIBLE
            if (::timer.isInitialized) {

                startTimer()
            }
        } else if (currentQuestionIndex == questions.size - 1) {

            binding.finishButton.visibility = View.VISIBLE
            if (::timer.isInitialized) {

                startTimer()
            }
        }

        displayQuestion()
        updateQuestionNumber()
    }

    private fun updateQuestionNumber() {
        binding.questionNumberTextView.text = "Question ${currentQuestionIndex + 1}"
    }
}

