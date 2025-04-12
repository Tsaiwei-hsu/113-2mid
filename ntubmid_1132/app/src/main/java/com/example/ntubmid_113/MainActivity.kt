package com.example.ntubmid_113

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ntubmid_113.PizzaOrderActivity
import com.example.ntubmid_113.SideOrderActivity
import com.example.ntubmid_113.StoreInfoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var txtOrderSummary: TextView
    private lateinit var btnMainDish: Button
    private lateinit var btnSideDish: Button
    private lateinit var btnRestaurant: Button

    private val mainDishList = mutableListOf<String>()
    private val sideDishList = mutableListOf<String>()

    private val mainDishLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedList = result.data?.getStringArrayListExtra("SELECTED_DISH_LIST")
            selectedList?.let {
                mainDishList.clear()
                mainDishList.addAll(it)
                updateOrderSummary()
            }
        }
    }

    private val sideDishLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedList = result.data?.getStringArrayListExtra("SELECTED_DISH_LIST")
            selectedList?.let {
                sideDishList.clear()
                sideDishList.addAll(it)
                updateOrderSummary()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtOrderSummary = findViewById(R.id.orderSummaryTextView)
        btnMainDish = findViewById(R.id.btnMainDish)
        btnSideDish = findViewById(R.id.btnSideDish)
        btnRestaurant = findViewById(R.id.btnRestaurant)

        btnMainDish.setOnClickListener {
            val intent = Intent(this, PizzaOrderActivity::class.java)
            mainDishLauncher.launch(intent)
        }

        btnSideDish.setOnClickListener {
            val intent = Intent(this, SideOrderActivity::class.java)
            sideDishLauncher.launch(intent)
        }

        btnRestaurant.setOnClickListener {
            val intent = Intent(this, StoreInfoActivity::class.java)
            startActivity(intent)
        }

        updateOrderSummary()
    }

    private fun updateOrderSummary() {
        val main = if (mainDishList.isNotEmpty()) mainDishList.joinToString("\n") else "尚未選擇主餐"
        val side = if (sideDishList.isNotEmpty()) sideDishList.joinToString("\n") else "尚未選擇副餐"
        txtOrderSummary.text = "主餐:\n$main\n\n副餐:\n$side"
    }
}
