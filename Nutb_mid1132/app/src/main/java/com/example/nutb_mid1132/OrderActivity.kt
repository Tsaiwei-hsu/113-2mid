package com.example.pizzaorderapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this).apply {
            text = "選擇夏威夷披薩"
            setOnClickListener {
                val result = Intent()
                result.putExtra("pizza", "夏威夷披薩")
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }

        setContentView(button)
    }
}
