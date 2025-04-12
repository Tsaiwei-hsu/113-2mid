package com.example.pizzaorderapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.loader.R

class MainActivity : AppCompatActivity() {

    private lateinit var orderTextView: TextView
    private lateinit var orderButton: Button
    private lateinit var sideDishButton: Button
    private lateinit var storeInfoButton: Button

    private var selectedPizza: String = "尚未選擇"
    private var selectedSide: String = "尚未選擇"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        orderTextView = findViewById(R.id.ordertxt)
        orderButton = findViewById(R.id.order_button)
        sideDishButton = findViewById(R.id.side_dish_button)
        storeInfoButton = findViewById(R.id.store_info_button)

        updateOrderSummary()

        orderButton.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            ActivityResultLauncher(intent, REQUEST_ORDER)
        }

        sideDishButton.setOnClickListener {
            val intent = Intent(this, SideDishActivity::class.java)
            ActivityResultLauncher(intent, REQUEST_SIDE)
        }

        storeInfoButton.setOnClickListener {
            val intent = Intent(this, StoreListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ORDER -> {
                    selectedPizza = data?.getStringExtra("pizza") ?: selectedPizza
                }
                REQUEST_SIDE -> {
                    selectedSide = data?.getStringExtra("side") ?: selectedSide
                }
            }
            updateOrderSummary()
        }
    }

    private fun updateOrderSummary() {
        val summary = "主餐: $selectedPizza\n副餐: $selectedSide"
        orderSummaryTextView.text = summary
    }

    companion object {
        private const val REQUEST_ORDER = 1001
        private const val REQUEST_SIDE = 1002
    }
}
