package com.example.pizzaorderapp

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pizzaorderapp.adapters.OrderItemAdapter
import com.example.pizzaorderapp.databinding.ActivityMainBinding
import com.example.pizzaorderapp.db.AppDatabase
import com.example.pizzaorderapp.models.OrderItem
import com.example.pizzaorderapp.models.OrderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var pizzaAdapter: OrderItemAdapter
    private lateinit var sideAdapter: OrderItemAdapter
    private val pizzaItems = mutableListOf<OrderItem>()
    private val sideItems = mutableListOf<OrderItem>()
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // 初始化數據庫
        db = AppDatabase.getDatabase(applicationContext)

        setupRecyclerViews()
        setupButtons()
        loadOrderItems()
    }

    private fun setupRecyclerViews() {
        // 披薩列表
        pizzaAdapter = OrderItemAdapter(pizzaItems) { item ->
            // 點擊刪除按鈕時的處理
            removeOrderItem(item)
        }
        binding.pizzaRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = pizzaAdapter
        }

        // 副餐列表
        sideAdapter = OrderItemAdapter(sideItems) { item ->
            // 點擊刪除按鈕時的處理
            removeOrderItem(item)
        }
        binding.sideRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = sideAdapter
        }
    }

    private fun setupButtons() {
        // 點餐按鈕
        binding.orderPizzaButton.setOnClickListener {
            val intent = Intent(this, PizzaMenuActivity::class.java)
            startActivity(intent)
        }

        // 副餐按鈕
        binding.orderSideButton.setOnClickListener {
            val intent = Intent(this, SideMenuActivity::class.java)
            startActivity(intent)
        }

        // 店家信息按鈕
        binding.storeInfoButton.setOnClickListener {
            val intent = Intent(this, StoreInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadOrderItems() {
        CoroutineScope(Dispatchers.IO).launch {
            // 從數據庫加載訂單項目
            val allItems = db.orderItemDao().getAllItems()

            val pizzas = allItems.filter { it.type == OrderType.PIZZA }
            val sides = allItems.filter { it.type == OrderType.SIDE }

            withContext(Dispatchers.Main) {
                // 更新UI
                pizzaItems.clear()
                pizzaItems.addAll(pizzas)
                pizzaAdapter.notifyDataSetChanged()

                sideItems.clear()
                sideItems.addAll(sides)
                sideAdapter.notifyDataSetChanged()

                updateTotalPrice()
                updateVisibility()
            }
        }
    }

    private fun removeOrderItem(item: OrderItem) {
        CoroutineScope(Dispatchers.IO).launch {
            // 從數據庫刪除項目
            db.orderItemDao().delete(item)

            withContext(Dispatchers.Main) {
                // 從列表中移除
                if (item.type == OrderType.PIZZA) {
                    pizzaItems.remove(item)
                    pizzaAdapter.notifyDataSetChanged()
                } else {
                    sideItems.remove(item)
                    sideAdapter.notifyDataSetChanged()
                }

                updateTotalPrice()
                updateVisibility()
            }
        }
    }

    private fun updateTotalPrice() {
        // 計算總價
        val pizzaTotal = pizzaItems.sumOf { it.price * it.quantity }
        val sideTotal = sideItems.sumOf { it.price * it.quantity }
        val total = pizzaTotal + sideTotal

        // 更新UI
        binding.totalPriceTextView.text = getString(R.string.total_price_format, total)
    }

    private fun updateVisibility() {
        // 根據列表是否為空來更新視圖可見性
        if (pizzaItems.isEmpty() && sideItems.isEmpty()) {
            binding.emptyOrderLayout.visibility = View.VISIBLE
            binding.orderContentLayout.visibility = View.GONE
        } else {
            binding.emptyOrderLayout.visibility = View.GONE
            binding.orderContentLayout.visibility = View.VISIBLE

            // 更新披薩和副餐列表的可見性
            if (pizzaItems.isEmpty()) {
                binding.pizzaSection.visibility = View.GONE
            } else {
                binding.pizzaSection.visibility = View.VISIBLE
            }

            if (sideItems.isEmpty()) {
                binding.sideSection.visibility = View.GONE
            } else {
                binding.sideSection.visibility = View.VISIBLE
            }
        }
    }

    // 活動恢復時刷新訂單數據
    override fun onResume() {
        super.onResume()
        loadOrderItems()
    }
}