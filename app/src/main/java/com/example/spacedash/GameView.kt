package com.example.spacedash

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

//game interface
class GameView(var c:Context,var gameTask: GameTask):View(c) {
    //initialize variable
    private var myPaint:Paint? = null
    private  var speed =1
    private  var  time = 0
    private  var score = 0
    private var highScore = 0
    private var myCarPosition = 0
    private  val otherCars = ArrayList<HashMap<String,Any>>()

    //view dimension
    var viewWidth = 0
    var viewHeight = 0

    private val sharedPref: SharedPreferences =
        c.getSharedPreferences("HighScorePref", Context.MODE_PRIVATE)

    init {
        myPaint = Paint()
        highScore = sharedPref.getInt("high_score", 0)
    }

    override fun onDraw (canvas: Canvas){
        super.onDraw(canvas)

        //Update view
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if(time % 700 < 10 +speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()
            map["startTime"]= time
            otherCars.add(map)
        }

        //UFO
        time = time+ 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.ufo, null)


        val ufoWidth = carWidth * 3/2
        val ufoHeight = carHeight * 2

        d.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - ufoHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25 + ufoWidth,
            viewHeight - 2
        )


        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN


        for(i in otherCars.indices){
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth /15
                var carY = time - otherCars[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.spacerock,null)

                d2.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                d2.draw(canvas)
                if (otherCars[i]["lane"] as Int == myCarPosition){

                    if (carY>viewHeight -2 - carHeight
                        && carY < viewHeight-2){

                        gameTask.closeGame(score)
                    }
                }

                if (carY > viewHeight + carHeight){
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score/8)

                    if(score > highScore){
                        highScore = score
                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }

        //score and high score
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint!!)
        canvas.drawText("High Score: $highScore", 80f, 140f, myPaint!!)
        invalidate()
    }

    //touch events
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
               val x1 = event.x
                if (x1<viewWidth/2){
                    if (myCarPosition>0){
                        myCarPosition--
                    }
                }
                if (x1>viewWidth/2){
                    if (myCarPosition<2){
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP ->{

            }
        }
        return true
    }

    //reset game
    fun resetGame() {
        time = 0
        score = 0
        speed = 1
        myCarPosition = 0
        otherCars.clear()

        //redraw view
        invalidate()
    }

    //update high score
    private fun updateHighScore(newScore: Int) {
        if (newScore > highScore) {
            highScore = newScore
            // Save high score to SharedPreferences
            with(sharedPref.edit()) {
                putInt("high_score", highScore)
                apply()
            }
        }
    }

}