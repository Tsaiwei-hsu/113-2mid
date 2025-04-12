package com.example.first0312

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtname = findViewById<TextView>(R.id.name)
        val txtage = findViewById<TextView>(R.id.age)
        val btnGo = findViewById<Button>(R.id.btnGo)
        btnGo.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("name", txtname.text.toString())
            intent.putExtra("Age", txtage.text.toString().toInt())
            startActivity(intent)
        }
        val btn4 = findViewById<Button>(R.id.btnGo4)
        btn4.setOnClickListener {
            /*
            startActivity(Intent(Intent.ACTION_DIAL).apply{
                data = android.net.Uri.parse("tel:0987654321")
            })
             */
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.ntub.edu.tw")
            })
        }
        val btn7 = findViewById<Button>(R.id.btnGo7)
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val data = result.data?.getStringExtra("resultKey")
                Log.d("MainActivity", "onCreate: got data: $data")
            }
        }
        btn7.setOnClickListener {
            launcher.launch(Intent(this, MainActivity2::class.java))
        }
        val imageView = findViewById<ImageView>(R.id.image01)
        imageView.setImageResource(R.drawable.img_8340)

        val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
            imageView.setImageURI(uri)
        }
        imageView.setOnClickListener{
            imageLauncher.launch("image/*")
        }
    }
}