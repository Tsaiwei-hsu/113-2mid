package com.example.pizzaorderapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SideDishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this).apply {
            text = "選擇薯條"
            setOnClickListener {
                val result = Intent()
                result.putExtra("side", "薯條")
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }

        setContentView(button)
    }
}
