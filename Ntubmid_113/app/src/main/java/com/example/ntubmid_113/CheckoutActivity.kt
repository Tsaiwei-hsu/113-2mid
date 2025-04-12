package com.example.ntubmid_113

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    // 用來接收付款結果的 Launcher
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // 取得畫面button
        val txtSummary = findViewById<TextView>(R.id.checkoutSummaryTextView)
        val btnPay = findViewById<Button>(R.id.btnPay)
        val btnBackToMain = findViewById<Button>(R.id.btnBackToMain)

        // 返回主畫面button
        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("CLEAR_ORDER", false) // 或 true，如果你想順便清空訂單
            }
            startActivity(intent)
            finish()
        }

        // 接收主餐與副餐資料
        val mainDishList = intent.getStringArrayListExtra("MAIN_DISH_LIST") ?: arrayListOf()
        val sideDishList = intent.getStringArrayListExtra("SIDE_DISH_LIST") ?: arrayListOf()

        // 訂單明細
        val totalSummary = buildString {
            append("【主餐】\n")
            append(mainDishList.joinToString("\n").ifEmpty { "尚未選擇主餐" })
            append("\n\n【副餐】\n")
            append(sideDishList.joinToString("\n").ifEmpty { "尚未選擇副餐" })
        }

        // 顯示訂單內容
        txtSummary.text = totalSummary

        // 註冊付款頁面結果處理
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // 如果付款成功就通知主畫面，並且要求清除訂單
            if (result.resultCode == RESULT_OK && result.data?.getBooleanExtra("CLEAR_ORDER", false) == true) {
                setResult(RESULT_OK, Intent().putExtra("CLEAR_ORDER", true))
                finish()
            }
        }

        // 前往付款方式選擇畫面
        btnPay.setOnClickListener {
            val intent = Intent(this, PaymentOptionActivity::class.java)
            launcher.launch(intent)
        }
    }
}
