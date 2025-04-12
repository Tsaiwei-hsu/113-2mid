package com.example.a0319

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnToast = findViewById<Button>(R.id.btnToast)
        btnToast.setOnClickListener {
            val toast = Toast.makeText(this, "你成功了！", Toast.LENGTH_LONG)
            //toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        val btnSnackbar = findViewById<Button>(R.id.btnSnackbar)
        btnSnackbar.setOnClickListener { it ->
            Snackbar.make(it, "我是Snackbar！", Snackbar.LENGTH_LONG)
                .setAction("了解") {

                }
                .show()
        }
        val btnDialog = findViewById<Button>(R.id.btnDialog)
        btnDialog.setOnClickListener { it ->
            AlertDialog.Builder(this)
                .setTitle("標題")
                .setMessage("呈現內容")
                .setPositiveButton("確認") { dialog, which ->

                }.setNeutralButton("取消") { dialog, which ->

                }
                .show()
        }
        val btnListDialog = findViewById<Button>(R.id.btnListDialog)
        val items = arrayOf("1", "2", "3", "4", "5")
        btnListDialog.setOnClickListener { it ->
            AlertDialog.Builder(this)
                .setTitle("標題")
                .setItems(items) { dialog, i ->
                    Snackbar.make(it, "你剛按了 ${items[i]} 這個選項", Snackbar.LENGTH_LONG)
                        .show()
                }
                .setNeutralButton("取消") { dialog, which ->

                }
                .show()
        }
        val btnSingleItem = findViewById<Button>(R.id.btnSingleItem)
        btnSingleItem.setOnClickListener { it ->
            var pos = 0
            AlertDialog.Builder(this)
                .setTitle("標題")
                .setSingleChoiceItems(items, pos) { dialog, i ->
                    Snackbar.make(it, "你剛按了 ${items[i]} 這個選項", Snackbar.LENGTH_SHORT)
                        .show()
                }
                .setNeutralButton("取消") { dialog, which ->

                }
                .show()
        }
        val btndialog = findViewById<Button>(R.id.btndialog)
        btndialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setView(R.layout.dialog_contact)
                .setPositiveButton("新增") { dialog, which ->
                    val mdialog = dialog as AlertDialog
                    val name = mdialog.findViewById<TextView>(R.id.edtname)
                    val phone = mdialog.findViewById<TextView>(R.id.edtPhone)
                    Log.d("AlerDialog", "onCreate: name: ${name?.text}, phone: ${phone?.text}")
                }
                .show()
        }
        val listView = findViewById<ListView>(R.id.ListView)
        val items2 =
            arrayListOf(
                arrayOf("zzy", "內容"),
                arrayOf("0416","內容" ),
                arrayOf("sdfj", "內容"),
                arrayOf("wei","內容" ),
                arrayOf("55","內容" ),
                arrayOf("210219","內容" )
            )

        /*val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items2)*/
        val arrayAdapter2 = object : ArrayAdapter<Array<String>>(
            this, android.R.layout.simple_list_item_2, android.R.id.text1, items2){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                view.findViewById<TextView>(android.R.id.text1).text = items2[position][0]
                view.findViewById<TextView>(android.R.id.text2).text = items2[position][1]
                return view
            }
        }
        val items3 = ArrayList<Item>()
        /*
        items3.add(Item(R.drawable.photo_1, "G"))
        items3.add(Item(R.drawable.photo_2, "Ge"))*/

        class MyAdapter(context: Context, val layoutId: Int, val data: ArrayList<Item>)
            : ArrayAdapter<Item>(context, layoutId, data){
            override fun getCount() = data.size
            override fun getItem(position: Int) = data[position]
            override fun getItemId(position: Int) = 0L
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = View.inflate(parent.context, layoutId, null)
                val item = getItem(position)?:return view
                view.findViewById<ImageView>(R.id.imgPhoto).setImageResource(item.photo)
                view.findViewById<TextView>(R.id.txtName).text = item.name
                return view
            }}

        val myAdapter = MyAdapter(this, R.layout.adapter_item, items3)
        listView.adapter = myAdapter

        btnDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setView(R.layout.dialog_contact)
                .setPositiveButton("新增"){ dialog, which ->
                    val mdialog = dialog as AlertDialog
                    val name = mdialog.findViewById<TextView>(R.id.edtname)
                    val phone = mdialog.findViewById<TextView>(R.id.edtPhone)
                    Log.d("AlreDialog", "onCreate: name: ${name?.text}, phone: ${phone?.text}")
                    val img = arrayOf(
                        R.drawable.photo_1,
                        R.drawable.photo_2,
                        R.drawable.photo_3).random()
                    items3.add(Item(img, name!!.text.toString()))
                    Snackbar.make(it, "新增資料 ${name.text.toString()} 成功", Snackbar.LENGTH_LONG).show()
                    myAdapter.notifyDataSetChanged()
                }
                .show()
        }

        /*listView.adapter = arrayAdapter2*/
        listView.setOnItemClickListener { adapterView, view, i, l ->
            Snackbar
                .make(view, "你點了 ${items2 [i][0]} ", Snackbar.LENGTH_LONG)
                .setAction("好的") {

                }.show()
        }
    }
}