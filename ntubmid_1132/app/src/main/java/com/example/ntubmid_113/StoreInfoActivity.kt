package com.example.ntubmid_113

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.appsearch.StorageInfo
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StoreInfoActivity : AppCompatActivity() {
    private lateinit var restaurantList: MutableList<StorageInfo>
    private lateinit var adapter: StorInfoAdapter
    private lateinit var listView: ListView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_info)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ← 返回鍵

        findViewById<Button>(R.id.Mainbtn).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        listView = findViewById(R.id.restaurantListView)
        restaurantList = loadRestaurants()

        adapter = StorInfoAdapter(this, R.layout.list_item_restaurant, restaurantList)
        listView.adapter = adapter

        // 點一下撥號
        listView.setOnItemClickListener { _, _, position, _ ->
            val phone = restaurantList[position].phone
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        }

        // 長按：編輯或刪除
        listView.setOnItemLongClickListener { _, _, position, _ ->
            showOptionsDialog(position)
            true
        }

        findViewById<Button>(R.id.addButton).setOnClickListener {
            showAddDialog()
        }
    }

    private fun showOptionsDialog(position: Int) {
        val options = arrayOf("編輯", "刪除")
        AlertDialog.Builder(this)
            .setTitle("操作選項")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditDialog(position)
                    1 -> confirmDelete(position)
                }
            }
            .show()
    }

    private fun confirmDelete(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("刪除店家")
            .setMessage("確定要刪除 ${restaurantList[position].name} 嗎？")
            .setPositiveButton("刪除") { _, _ ->
                restaurantList.removeAt(position)
                adapter.notifyDataSetChanged()
                saveRestaurants()
            }
            .setNegativeButton("取消", null)
            .show()
    }

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



    private fun showEditDialog(position: Int) {
        val view = layoutInflater.inflate(R.layout.dialog_restaurant, null)
        val nameInput = view.findViewById<EditText>(R.id.editName)
        val phoneInput = view.findViewById<EditText>(R.id.editPhone)

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

    private fun saveRestaurants() {
        val prefs = getSharedPreferences("restaurant_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        val set = restaurantList.map { "${it.name}|${it.phone}" }.toSet()
        editor.putStringSet("restaurant_data", set)
        editor.apply()
    }

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

    override fun onSupportNavigateUp(): Boolean {
        finish() // ← 返回主畫面
        return true
    }
}
