package com.dipali.quizapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dipali.quizapp.databinding.ActivityMainLayoutBinding
import com.dipali.quizapp.ui.QuizActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playQuizButton.setOnClickListener {

            onPlayGameButtonClick(it)
        }
    }

    private fun onPlayGameButtonClick(view: View) {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}
