package com.example.ntubmid_113

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PaymentResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_result)

        //付款後頁面
        val isCard = intent.getBooleanExtra("IS_CARD", true)
        val message = if (isCard) {
            "✅ 信用卡付款成功！"
        } else {
            "🧾 請至櫃檯完成付款"
        }

        val textView = findViewById<TextView>(R.id.txtPaymentResult)
        textView.text = message

        // 等待 2 秒後回傳資料給CheckoutActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val resultIntent = Intent().apply {
                putExtra("CLEAR_ORDER", true)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }, 2000)

    }
}
