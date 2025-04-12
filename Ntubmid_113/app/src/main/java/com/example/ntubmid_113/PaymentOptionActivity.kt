package com.example.ntubmid_113

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PaymentOptionActivity : AppCompatActivity() {

    private lateinit var resultLauncher: androidx.activity.result.ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_option)

        val btnCard = findViewById<Button>(R.id.btnCreditCard)
        val btnCounter = findViewById<Button>(R.id.btnCounter)

        // 註冊接收 PaymentResult 回傳
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.getBooleanExtra("CLEAR_ORDER", false) == true) {
                setResult(Activity.RESULT_OK, result.data) // 傳回給 CheckoutActivity
                finish()
            }
        }
        //信用卡付款
        btnCard.setOnClickListener {
            val intent = Intent(this, PaymentResultActivity::class.java)
            intent.putExtra("IS_CARD", true)
            resultLauncher.launch(intent)
        }
        //櫃檯結帳
        btnCounter.setOnClickListener {
            val intent = Intent(this, PaymentResultActivity::class.java)
            intent.putExtra("IS_CARD", false)
            resultLauncher.launch(intent)
        }
    }
}
