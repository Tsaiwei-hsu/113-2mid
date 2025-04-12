package com.example.first0312

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2() : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*val edtPassword = findViewById<EditText>(R.id.edtpassword)
        val btnOK = findViewById<Button>(R.id.btnOK)
        val btncancel = findViewById<Button>(R.id.btncancel)*/



        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data?.getStringExtra("resultKey")
                    Log.d("MainActivity", "=> 收到回傳: $data")
                }
            }
        val btnGo7 = findViewById<Button>(R.id.btnGo7)
        btnGo7.setOnClickListener {
            launcher.launch(Intent(this, MainActivity2::class.java))
        }
    }
}