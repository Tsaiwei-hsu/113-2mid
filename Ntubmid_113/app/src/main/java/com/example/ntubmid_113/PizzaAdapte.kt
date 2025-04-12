package com.example.ntubmid_113

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

// Adapter 用於將 pizzaList 資料顯示在 RecyclerView 上
class PizzaAdapter(
    private val pizzaList: List<Pizza>,           // 傳入 Pizza 清單
    private val onSelectionChanged: () -> Unit    // 當數量改變時要呼叫的 callback
) : RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder>() {

    // 綁定畫面中的元件
    inner class PizzaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.dishImageView)         // 餐點圖片
        val name: TextView = itemView.findViewById(R.id.dishNameTextView)        // 餐點名稱
        val price: TextView = itemView.findViewById(R.id.dishPriceTextView)      // 餐點價格
        val quantity: TextView = itemView.findViewById(R.id.dishQuantityTextView)// 餐點數量顯示
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)                // + 按鈕
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)              // - 按鈕
        val cardView: CardView = itemView.findViewById(R.id.cardView)            // 外層卡片容器
    }

    // 建立list_item_pizza.xml的畫面
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_pizza, parent, false)
        return PizzaViewHolder(view)
    }

    // 回傳資料總筆數
    override fun getItemCount(): Int = pizzaList.size

    // 綁定資料到畫面元件
    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        val pizza = pizzaList[position]
        holder.image.setImageResource(pizza.imageResId)             // 設定圖片
        holder.name.text = pizza.name                               // 設定名稱
        holder.price.text = pizza.price                             // 設定價格
        holder.quantity.text = pizza.quantity.toString()            // 顯示數量

        // 加號按鈕：數量 +1，更新顯示與通知外部 callback
        holder.btnPlus.setOnClickListener {
            pizza.quantity++
            holder.quantity.text = pizza.quantity.toString()
            onSelectionChanged() // 通知主畫面更新已選項目或按鈕狀態
        }

        // 減號按鈕：數量 -1（不可低於 0）
        holder.btnMinus.setOnClickListener {
            if (pizza.quantity > 0) {
                pizza.quantity--
                holder.quantity.text = pizza.quantity.toString()
                onSelectionChanged()
            }
        }
    }
}
