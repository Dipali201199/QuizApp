package com.dipali.quizapp.ui.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

data class Question(
    val id: Int,
    val questionText: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz_database"
        private const val DATABASE_VERSION = 2

        private const val TABLE_NAME = "questions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_QUESTION_TEXT = "questionText"
        private const val COLUMN_CORRECT_ANSWER = "correctAnswer"
        private const val COLUMN_INCORRECT_ANSWERS = "incorrectAnswers"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the "questions" table

        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_QUESTION_TEXT TEXT,
                $COLUMN_CORRECT_ANSWER TEXT,
                $COLUMN_INCORRECT_ANSWERS TEXT
            )
        """.trimIndent()

        try {
            db.execSQL(createTableQuery)
            insertSampleQuestions(db)
        } catch (e: SQLiteException) {
            Log.e("DatabaseHelper", "Error creating database or inserting sample data", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    private fun insertSampleQuestions(db: SQLiteDatabase) {


        val questions = listOf(
            Question(
                1,
                "Who is the Prime Minister of India?",
                "Narendra Modi",
                listOf("Rahul Gandhi", "Manmohan Singh", "Amit Shah")
            ),
            Question(
                2,
                "What is the capital of India?",
                "Delhi",
                listOf("Mumbai", "Chennai", "Ahmedabad")
            ),
            Question(3, "What is sum of 15 + 25 ?", "40", listOf("5", "25", "None")),
            Question(
                4,
                "Which one is maximum? 25, 11, 17, 18, 40, 42",
                "42",
                listOf("11", "25", "None")
            ),
            Question(
                5,
                "What is the official language of Gujarat?",
                "Gujarati",
                listOf("Hindi", "Marathi", "None")
            ),
            Question(6, "What is multiplication of 12 * 12 ?", "None", listOf("124", "12", "24")),
            Question(
                7,
                "Which state of India has the largest population?",
                "UP",
                listOf("Bihar", "Gujarat", "Maharashtra")
            ),
            Question(
                8,
                "Who is the Home Minister of India?",
                "Amit Shah",
                listOf("Rajnath Singh", "Narendra Modi", "None")
            ),
            Question(
                9,
                "What is the capital of Gujarat?",
                "Gandhinagar",
                listOf("Vadodara", "Ahmedabad", "Rajkot")
            ),
            Question(10, "Which one is minimum? 5, 0, -20, 11", "-20", listOf("0", "11", "None")),
            Question(11, "What is sum of 10, 12 and 15?", "37", listOf("25", "10", "12")),
            Question(
                12,
                "What is the official language of the Government of India?",
                "Hindi",
                listOf("English", "Gujarati", "None")
            ),
            Question(13, "Which country is located in Asia?", "India", listOf("USA", "UK", "None")),
            Question(
                14,
                "Which language(s) is/are used for Android app development?",
                " Java & Kotlin",
                listOf("Java", "Kotlin", "Swift")
            ),
            Question(
                14,
                "Which number will be next in series? 1, 4, 9, 16, 25",
                " 36",
                listOf("21", "49", "32")
            ),
        )


        for (question in questions) {
            insertQuestion(db, question)
        }


    }


    @SuppressLint("Range")

     fun getRandomQuestions(count: Int): List<Question> {
          Log.d("DatabaseHelper", "Entering getRandomQuestions")

          val questions: MutableList<Question> = ArrayList()
         val query = "SELECT * FROM $TABLE_NAME ORDER BY RANDOM() LIMIT $count"
          Log.d("DatabaseHelper", "Query: $query")

          val db = this.readableDatabase
          val cursor: Cursor = db.rawQuery(query, null)

          if (cursor.moveToFirst()) {
              do {
                  Log.d("DatabaseHelper", "Row: ${cursor.getString(0)}, ${cursor.getString(1)}, ...")

                  val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                  val questionText = cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_TEXT))
                  val correctAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_CORRECT_ANSWER))
                  val incorrectAnswersString =
                      cursor.getString(cursor.getColumnIndex(COLUMN_INCORRECT_ANSWERS))


                  val incorrectAnswers = incorrectAnswersString.split(",")

                  val question = Question(id, questionText, correctAnswer, incorrectAnswers)
                  questions.add(question)
              } while (cursor.moveToNext())
          }

          cursor.close()
          db.close()

          return questions
      }


    private fun insertQuestion(db: SQLiteDatabase, question: Question) {
        val values = ContentValues().apply {
            put(COLUMN_ID, question.id)
            put(COLUMN_QUESTION_TEXT, question.questionText)
            put(COLUMN_CORRECT_ANSWER, question.correctAnswer)
            put(COLUMN_INCORRECT_ANSWERS, question.incorrectAnswers.joinToString(","))

        }

        try {
            db.insertOrThrow(TABLE_NAME, null, values)
        } catch (e: SQLiteException) {
            Log.e("DatabaseHelper", "Error inserting question", e)
        }
    }


}
