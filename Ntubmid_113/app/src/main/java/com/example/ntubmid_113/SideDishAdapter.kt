package com.example.ntubmid_113

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

// Adapter 用於在 RecyclerView 中顯示副餐清單
class SideDishAdapter(
    private val sideDishList: List<SideDish>,          // 傳入副餐資料清單
    private val onSelectionChanged: () -> Unit          // 每當數量變動時呼叫的 callback
) : RecyclerView.Adapter<SideDishAdapter.SideDishViewHolder>() {

    // 自訂 ViewHolder，綁定 list_item_side_dish 中的元件
    inner class SideDishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.dishImageView)
        val name: TextView = itemView.findViewById(R.id.dishNameTextView)
        val price: TextView = itemView.findViewById(R.id.dishPriceTextView)
        val quantity: TextView = itemView.findViewById(R.id.dishQuantityTextView)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    // 建立新的 ViewHolder，載入 item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SideDishViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_side_dish, parent, false)
        return SideDishViewHolder(view)
    }

    // 傳回清單項目數
    override fun getItemCount(): Int = sideDishList.size

    // 綁定資料到畫面元件
    override fun onBindViewHolder(holder: SideDishViewHolder, position: Int) {
        val item = sideDishList[position]

        // 設定每個副餐項目的圖片、文字與數量
        holder.image.setImageResource(item.imageResId)
        holder.name.text = item.name
        holder.price.text = item.price
        holder.quantity.text = item.quantity.toString()

        // 數量加 1，更新畫面與通知外部 callback
        holder.btnPlus.setOnClickListener {
            item.quantity++
            holder.quantity.text = item.quantity.toString()
            onSelectionChanged() // 通知活動更新顯示與按鈕狀態
        }

        // 數量減 1（最小為 0），更新畫面與通知
        holder.btnMinus.setOnClickListener {
            if (item.quantity > 0) {
                item.quantity--
                holder.quantity.text = item.quantity.toString()
                onSelectionChanged()
            }
        }
    }
}
