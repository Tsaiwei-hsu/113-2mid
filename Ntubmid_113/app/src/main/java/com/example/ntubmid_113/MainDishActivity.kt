package com.example.ntubmid_113

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainDishActivity : AppCompatActivity() {

    // 宣告畫面元件
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedTextView: TextView
    private lateinit var btnConfirm: Button
    private lateinit var btnBackToMain: Button

    // 主餐列表
    private val pizzaList = listOf(
        Pizza("瑪格麗特 Pizza", "$180", R.drawable.pizza_margherita),
        Pizza("夏威夷 Pizza", "$200", R.drawable.pizza_hawaiian),
        Pizza("海鮮總匯 Pizza", "$250", R.drawable.pizza_seafood),
        Pizza("辣味臘腸 Pizza", "$220", R.drawable.pizza_pepperoni),
        Pizza("起司火山 Pizza", "$240", R.drawable.pizza_cheese)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dish)

        // 取得畫面元件
        recyclerView = findViewById(R.id.pizzaRecyclerView)
        selectedTextView = findViewById(R.id.selectedItemsTextView)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnBackToMain = findViewById(R.id.btnBackToMain)

        // 建立 Adapter，並傳入 callback，讓點擊後能更新畫面顯示
        val adapter = PizzaAdapter(pizzaList) {
            updateSelectedText()
            updateConfirmButtonState()
        }

        // 設定 RecyclerView 直式排列與 Adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 點擊「完成選擇」按鈕後，回傳已選的餐點清單
        btnConfirm.setOnClickListener {
            val selected = pizzaList.filter { it.quantity > 0 } // 篩選有數量的項目
            val resultList = selected.map { "${it.name} - ${it.price} x${it.quantity}" }

            // 傳資料回主畫面
            val intent = Intent().apply {
                putStringArrayListExtra("SELECTED_DISH_LIST", ArrayList(resultList))
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // 點擊返回主畫面按鈕
        btnBackToMain.setOnClickListener {
            finish()
        }

        // 初始化畫面資訊
        updateSelectedText()
        updateConfirmButtonState()
    }

    // 更新畫面上「已選擇」的文字
    private fun updateSelectedText() {
        val selected = pizzaList.filter { it.quantity > 0 }
        selectedTextView.text = if (selected.isEmpty()) {
            "已選擇：無"
        } else {
            "已選擇：${selected.joinToString("、") { "${it.name} x${it.quantity}" }}"
        }
    }

    // 更新確認按鈕的狀態（是否啟用、透明度）
    private fun updateConfirmButtonState() {
        val hasSelection = pizzaList.any { it.quantity > 0 }
        btnConfirm.isEnabled = hasSelection
        btnConfirm.alpha = if (hasSelection) 1f else 0.5f
    }

    // 處理 ActionBar 返回鍵邏輯
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
