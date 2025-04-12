package com.example.pizzaorderapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class StoreListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            text = "這是店家資訊頁（之後來實作 ListView）"
            textSize = 20f
            setPadding(30, 30, 30, 30)
        }
        setContentView(textView)
    }
}
