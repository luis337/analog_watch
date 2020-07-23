package com.luis.analogwatch

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        analogWatch.apply {
            colorShapeCircle = Color.RED
            colorAmPm = Color.DKGRAY
            colorShapeTime = Color.BLACK
            colorTime = Color.BLACK
            is24Hours = false
        }
        /*

        analogWatch.apply {
            colorShapeCircle = Color.BLACK
            colorAmPm = Color.LTGRAY
            colorShapeTime = Color.YELLOW
            colorTime = Color.GRAY
            is24Hours = true
        }

         */
    }
}