package com.example.ntubmid_113

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    // 宣告元件與資料儲存區
    private lateinit var orderSummaryTextView: TextView
    private lateinit var orderMainDishButton: Button
    private lateinit var orderSideDishButton: Button
    private lateinit var viewRestaurantsButton: Button

    // 儲存主餐與副餐選項
    private val mainDishes = mutableListOf<String>()
    private val sideDishes = mutableListOf<String>()

    // 主餐選擇頁面回傳結果的處理器
    private val mainDishLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedList = result.data?.getStringArrayListExtra("SELECTED_DISH_LIST")
            selectedList?.forEach {
                mainDishes.add(it)
                Snackbar.make(findViewById(android.R.id.content), "🍕 主餐已選: $it", Snackbar.LENGTH_SHORT).show()
            }
            updateOrderSummary() // 更新訂單摘要
        }
    }

    // 副餐選擇頁面回傳結果的處理器
    private val sideDishLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedList = result.data?.getStringArrayListExtra("SELECTED_SIDE_LIST")
            selectedList?.forEach {
                sideDishes.add(it)
                Snackbar.make(findViewById(android.R.id.content), "🍟 副餐已選: $it", Snackbar.LENGTH_SHORT).show()
            }
            updateOrderSummary() // 更新訂單摘要
        }
    }

    // 結帳頁面回傳結果的處理器
    private val checkoutLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // 如果有要求清除訂單，則清空清單並更新畫面
        if (result.resultCode == RESULT_OK && result.data?.getBooleanExtra("CLEAR_ORDER", false) == true) {
            mainDishes.clear()
            sideDishes.clear()
            updateOrderSummary()
            Snackbar.make(findViewById(android.R.id.content), "✅ 訂單已清空", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 取得畫面元件
        orderSummaryTextView = findViewById(R.id.orderSummaryTextView)
        orderMainDishButton = findViewById(R.id.orderMainDishButton)
        orderSideDishButton = findViewById(R.id.orderSideDishButton)
        viewRestaurantsButton = findViewById(R.id.viewRestaurantsButton)

        // 點選選擇主餐按鈕，開啟主餐頁面
        orderMainDishButton.setOnClickListener {
            mainDishLauncher.launch(Intent(this, MainDishActivity::class.java))
        }

        // 點選選擇副餐按鈕，開啟副餐頁面
        orderSideDishButton.setOnClickListener {
            sideDishLauncher.launch(Intent(this, SideDishActivity::class.java))
        }

        // 查看店家資訊
        viewRestaurantsButton.setOnClickListener {
            startActivity(Intent(this, RestaurantListActivity::class.java))
        }

        // 結帳按鈕button
        findViewById<Button>(R.id.checkoutButton).setOnClickListener {
            if (mainDishes.isEmpty() && sideDishes.isEmpty()) {
                // 如果沒有選擇任何餐點，提示使用者
                Snackbar.make(it, "尚未選擇餐點！", Snackbar.LENGTH_SHORT).show()
            } else {
                // 有選擇的話，傳遞資料進入結帳頁面
                val intent = Intent(this, CheckoutActivity::class.java).apply {
                    putStringArrayListExtra("MAIN_DISH_LIST", ArrayList(mainDishes))
                    putStringArrayListExtra("SIDE_DISH_LIST", ArrayList(sideDishes))
                }
                checkoutLauncher.launch(intent)
            }
        }

        // 點擊清除訂單按鈕，手動清除目前餐點
        findViewById<Button>(R.id.clearOrderButton).setOnClickListener {
            clearOrder(it)
        }

        // 初始更新一次摘要
        updateOrderSummary()
    }

    // 更新畫面上的訂單摘要與總價
    private fun updateOrderSummary() {
        val summary = StringBuilder()
        var totalPrice = 0
        var totalItems = 0

        summary.append("【已選主餐】\n")
        if (mainDishes.isEmpty()) {
            summary.append("尚未選擇主餐\n")
        } else {
            mainDishes.forEach {
                summary.append("- $it\n")
                totalPrice += extractPrice(it) // 解析價格加總
                totalItems++
            }
        }

        summary.append("\n【已選副餐】\n")
        if (sideDishes.isEmpty()) {
            summary.append("尚未選擇副餐\n")
        } else {
            sideDishes.forEach {
                summary.append("- $it\n")
                totalPrice += extractPrice(it)
                totalItems++
            }
        }

        summary.append("\n共 $totalItems 項，總金額：$${totalPrice}")
        orderSummaryTextView.text = summary.toString()
    }

    // 從字串中擷取價格
    private fun extractPrice(text: String): Int {
        val regex = Regex("\\$(\\d+)")
        val match = regex.find(text)
        return match?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

    // 清除訂單並提示使用者
    fun clearOrder(view: View) {
        mainDishes.clear()
        sideDishes.clear()
        updateOrderSummary()
        Snackbar.make(view, "❌ 訂單已清空", Snackbar.LENGTH_SHORT).show()
    }

}
