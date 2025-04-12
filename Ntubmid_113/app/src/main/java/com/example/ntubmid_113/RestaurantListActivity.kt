package com.example.ntubmid_113

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var restaurantList: MutableList<Restaurant> // 餐廳資料清單
    private lateinit var adapter: RestaurantAdapter               // 自訂 Adapter
    private lateinit var listView: ListView                      // 顯示餐廳資料的 ListView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        // 回主畫面按鈕
        findViewById<Button>(R.id.Mainbtn).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        // 初始化 ListView 與資料
        listView = findViewById(R.id.restaurantListView)
        restaurantList = loadRestaurants() // 從偏好設定中載入資料

        // 設定 Adapter 並綁定至 ListView
        adapter = RestaurantAdapter(this, R.layout.list_item_restaurant, restaurantList)
        listView.adapter = adapter

        // 撥打電話
        listView.setOnItemClickListener { _, _, position, _ ->
            val phone = restaurantList[position].phone
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        }

        // 長按出現編輯或刪除選單
        listView.setOnItemLongClickListener { _, _, position, _ ->
            showOptionsDialog(position)
            true
        }

        // 新增店家按鈕
        findViewById<Button>(R.id.addButton).setOnClickListener {
            showAddDialog()
        }
    }

    // 編輯 and 刪除選單對話框
    private fun showOptionsDialog(position: Int) {
        val options = arrayOf("編輯", "刪除")
        AlertDialog.Builder(this)
            .setTitle("操作選項")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditDialog(position) // 編輯
                    1 -> confirmDelete(position)  // 刪除
                }
            }
            .show()
    }

    // 刪除店家前的確認框
    private fun confirmDelete(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("刪除店家")
            .setMessage("確定要刪除 ${restaurantList[position].name} 嗎？")
            .setPositiveButton("刪除") { _, _ ->
                restaurantList.removeAt(position)
                adapter.notifyDataSetChanged()
                saveRestaurants() // 儲存更新後資料
            }
            .setNegativeButton("取消", null)
            .show()
    }

    // 新增店家對話框
    private fun showAddDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_restaurant, null)
        AlertDialog.Builder(this)
            .setTitle("新增店家")
            .setView(view)
            .setPositiveButton("新增") { _, _ ->
                val name = view.findViewById<EditText>(R.id.editName).text.toString()
                val phone = view.findViewById<EditText>(R.id.editPhone).text.toString()
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    restaurantList.add(Restaurant(name, phone))
                    adapter.notifyDataSetChanged()
                    saveRestaurants()
                } else {
                    Toast.makeText(this, "請輸入店名與電話", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    // 編輯店家對話框
    private fun showEditDialog(position: Int) {
        val view = layoutInflater.inflate(R.layout.dialog_restaurant, null)
        val nameInput = view.findViewById<EditText>(R.id.editName)
        val phoneInput = view.findViewById<EditText>(R.id.editPhone)

        // 預填原本的店家資料
        nameInput.setText(restaurantList[position].name)
        phoneInput.setText(restaurantList[position].phone)

        AlertDialog.Builder(this)
            .setTitle("更新店家")
            .setView(view)
            .setPositiveButton("儲存") { _, _ ->
                val name = nameInput.text.toString()
                val phone = phoneInput.text.toString()
                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    restaurantList[position].name = name
                    restaurantList[position].phone = phone
                    adapter.notifyDataSetChanged()
                    saveRestaurants()
                } else {
                    Toast.makeText(this, "請輸入完整資訊", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }

    // 將餐廳清單儲存至 SharedPreferences
    private fun saveRestaurants() {
        val prefs = getSharedPreferences("restaurant_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        val set = restaurantList.map { "${it.name}|${it.phone}" }.toSet()
        editor.putStringSet("restaurant_data", set)
        editor.apply()
    }

    // 從 SharedPreferences 載入餐廳資料，若無資料則使用預設值
    private fun loadRestaurants(): MutableList<Restaurant> {
        val prefs = getSharedPreferences("restaurant_prefs", MODE_PRIVATE)
        val set = prefs.getStringSet("restaurant_data", null)
        return set?.map {
            val parts = it.split("|")
            Restaurant(parts[0], parts[1])
        }?.toMutableList() ?: mutableListOf(
            Restaurant("Pizza Panda", "0212345678"),
            Restaurant("Burger Boss", "0287654321")
        )
    }

    // 處理返回箭頭點擊事件
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
