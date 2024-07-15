package com.example.spacedash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.content.SharedPreferences

class MainActivity : AppCompatActivity(),GameTask {

    //declare variables
    lateinit var rootLayout:LinearLayout
    lateinit var startBtn:Button
    lateinit var mGameView: GameView
    lateinit var score:TextView
    lateinit var highscore:TextView
    lateinit var name:TextView
    lateinit var gameufo: ImageView
    lateinit var mainmenuBtn:Button
    lateinit var exitBtn:Button

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing
        startBtn = findViewById(R.id.startBtn)
        mainmenuBtn = findViewById(R.id.mainmenuBtn)
        exitBtn = findViewById(R.id.exitBtn)

        rootLayout = findViewById(R.id.rootLayout)

        score   = findViewById(R.id.score)
        highscore   = findViewById(R.id.highscore)
        name = findViewById(R.id.name)

        gameufo = findViewById(R.id.gameufo)

        mainmenuBtn.visibility = View.GONE

        sharedPref = getSharedPreferences("HighScorePref", MODE_PRIVATE)

        //Get the high score value and display it
        val savedHighScore = sharedPref.getInt("high_score", 0)
        highscore.text = "High Score: $savedHighScore"

        mGameView = GameView(this,this)

        //start game
        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.space2)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
            name.visibility = View.GONE
            gameufo.visibility = View.GONE
            highscore.visibility = View.GONE
            exitBtn.visibility = View.GONE

        }

        exitBtn.setOnClickListener {

            finish()
        }

        mainmenuBtn.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    //end game
    override fun closeGame(mScore: Int) {
        score.text = "Score : $mScore"
        rootLayout.removeView(mGameView)
        gameufo.visibility = View.VISIBLE
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        mainmenuBtn.visibility = View.VISIBLE
        highscore.visibility = View.VISIBLE

        //update high score
        val savedHighScore = sharedPref.getInt("high_score", 0)
        if (mScore > savedHighScore) {
            with(sharedPref.edit()) {
                putInt("high_score", mScore)
                apply()
            }
            highscore.text = "High Score: $mScore"
        }

        //restart game to replay
        startBtn.setOnClickListener {

            score.text = "Score : 0"
            mGameView.resetGame()


            mGameView.setBackgroundResource(R.drawable.space2)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
            name.visibility = View.GONE
            gameufo.visibility = View.GONE
            highscore.visibility = View.GONE
            mainmenuBtn.visibility = View.GONE
            exitBtn.visibility = View.GONE
        }
    }
}