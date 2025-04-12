package com.example.ntubmid_113

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SideDishActivity : AppCompatActivity() {

    // 宣告畫面元件
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedTextView: TextView
    private lateinit var btnConfirm: Button
    private lateinit var btnBackToMain: Button

    // 副餐清單，每個物件包含名稱、價格與圖片資源
    private val sideDishList = listOf(
        SideDish("薯條", "$60", R.drawable.side_fries),
        SideDish("洋蔥圈", "$70", R.drawable.side_onionrings),
        SideDish("凱薩沙拉", "$90", R.drawable.side_salad),
        SideDish("雞塊", "$80", R.drawable.side_nuggets),
        SideDish("起司球", "$85", R.drawable.side_cheeseballs)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_dish)

        // 取得元件實體
        recyclerView = findViewById(R.id.sideDishRecyclerView)
        selectedTextView = findViewById(R.id.selectedItemsTextView)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnBackToMain = findViewById(R.id.btnBackToMain)

        // 尚未選擇任何項目時，完成按鈕為不可點擊
        btnConfirm.isEnabled = false
        btnConfirm.alpha = 0.5f

        // 設定 RecyclerView 的 Adapter，當數量變動時更新畫面
        val adapter = SideDishAdapter(sideDishList) {
            updateSelectedText()
            updateConfirmButtonState()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 點擊「完成」按鈕後，回傳已選副餐資料
        btnConfirm.setOnClickListener {
            val selected = sideDishList.filter { it.quantity > 0 } // 只選取有數量的副餐
            val resultList = selected.map { "${it.name} - ${it.price} x${it.quantity}" }

            val intent = Intent().apply {
                putStringArrayListExtra("SELECTED_SIDE_LIST", ArrayList(resultList))
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // 點擊返回按鈕
        btnBackToMain.setOnClickListener {
            finish()
        }

        // 初始化顯示狀態
        updateSelectedText()
        updateConfirmButtonState()
    }

    // 顯示目前已選擇的副餐資訊
    private fun updateSelectedText() {
        val selected = sideDishList.filter { it.quantity > 0 }
        selectedTextView.text = if (selected.isEmpty()) {
            "已選擇：無"
        } else {
            "已選擇：${selected.joinToString("、") { "${it.name} x${it.quantity}" }}"
        }
    }

    // 根據是否有選擇項目，更新「完成」按鈕狀態
    private fun updateConfirmButtonState() {
        val hasSelection = sideDishList.any { it.quantity > 0 }
        btnConfirm.isEnabled = hasSelection
        btnConfirm.alpha = if (hasSelection) 1f else 0.5f
    }

    // 處理 ActionBar 返回鍵事件
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
