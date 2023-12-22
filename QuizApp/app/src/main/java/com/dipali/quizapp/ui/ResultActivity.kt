package com.dipali.quizapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dipali.quizapp.R
import com.dipali.quizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val totalScore = intent.getIntExtra("TOTAL_SCORE", 0)

        displayResultMessage(totalScore)

        // Set the total score in the totalScoreTextView
        binding.totalScoreTextView.text = getString(R.string.total_score, totalScore.toString())

        binding.playAgainButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayResultMessage(totalScore: Int) {
        val resultMessage = when {
            totalScore == 10 -> "Awesome. You are Genius. Congratulations you won the Game."
            totalScore >= 9 -> "You Won! Congratulations and Well Done."
            totalScore >= 7 -> "You Won! Congratulations."
            totalScore >= 5 -> "You Won!"
            totalScore in 3..4 -> "Well played but you failed. All The Best for Next Game."
            totalScore in 0..2 -> "Sorry, You failed."
            else -> "Unknown result message"
        }

        binding.resultMessageTextView.text = resultMessage

        if (totalScore in 0..4) {

            binding.resultMessageTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
        } else {
            // Reset text color for other messages
            binding.resultMessageTextView.setTextColor(ContextCompat.getColor(this, R.color.green))
        }

        if (totalScore in 0..4) {
            binding.playAgainButton.text = "Try again"
        } else {
            // Reset text color for other messages
            binding.playAgainButton.text = "Play again"
        }
    }
}
