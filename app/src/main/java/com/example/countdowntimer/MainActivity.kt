package com.example.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val REMAINING_TIME = "remaining time"
    val TITLE = "title"
    val START_TIME_IN_MILLIS: Long = 1 * 60 * 1000
    var remainingTime: Long = START_TIME_IN_MILLIS
    var timer: CountDownTimer? = null
    var isTimerRunning = false

    lateinit var titleTv: TextView
    lateinit var timerTv: TextView
    lateinit var startBtn: Button
    lateinit var resetTv: TextView
    lateinit var pb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        startBtn.setOnClickListener {
            if (!isTimerRunning) {
                startTimer(START_TIME_IN_MILLIS)
                titleTv.text = resources.getText(R.string.keep_going)
            }
        }

        resetTv.setOnClickListener {
            resetTimer()
        }
    }

    private fun resetTimer() {
        timer?.cancel()
        remainingTime = START_TIME_IN_MILLIS
        updateTimerText()
        titleTv.text = resources.getText(R.string.take_pomodoro)
        isTimerRunning = false
        pb.progress = 100
    }

    private fun startTimer(startTime: Long) {
        timer = object : CountDownTimer(startTime, 1 * 1000) {

            override fun onTick(timeLeft: Long) {
                remainingTime = timeLeft
                updateTimerText()
                pb.progress =
                    remainingTime.toDouble().div(START_TIME_IN_MILLIS.toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Finish !!", Toast.LENGTH_SHORT).show()
                isTimerRunning = false
            }
        }.start()
        isTimerRunning = true
    }

    private fun updateTimerText() {
        val minute = remainingTime.div(1000).div(60)
        val second = remainingTime.div(1000) % (60)
        val formattedTime = String.format("%02d:%02d", minute, second)
        timerTv.text = formattedTime
    }

    private fun initializeViews() {
        titleTv = findViewById(R.id.title_tv)
        timerTv = findViewById(R.id.time_tv)
        startBtn = findViewById(R.id.start_btn)
        resetTv = findViewById(R.id.reset_tv)
        resetTv = findViewById(R.id.reset_tv)
        pb = findViewById(R.id.progressBar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(REMAINING_TIME, remainingTime)
        outState.putString(TITLE, titleTv.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedTime = savedInstanceState.getLong(REMAINING_TIME)
        val savedTitle = savedInstanceState.getString(TITLE)
        if (savedTime != START_TIME_IN_MILLIS) startTimer(savedTime)
        titleTv.text=savedTitle
    }
}
