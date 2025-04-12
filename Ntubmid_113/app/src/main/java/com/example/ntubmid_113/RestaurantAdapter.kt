package com.example.ntubmid_113

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes

// 自訂 Adapter，負責將餐廳資料顯示在 ListView 上
class RestaurantAdapter(
    context: Context,                            // 呼叫這個 Adapter 的 Context
    @LayoutRes private val layoutRes: Int,       // 使用的項目版型 layout resource
    private val restaurantList: List<Restaurant> // 餐廳資料清單
) : ArrayAdapter<Restaurant>(context, layoutRes, restaurantList) {

    // 每列要如何顯示的邏輯（會被 ListView 自動呼叫）
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // 使用既有的 view (convertView) 或重新載入 layout
        val rowView = convertView ?: LayoutInflater.from(context)
            .inflate(layoutRes, parent, false)

        // 取得對應位置的餐廳資料
        val restaurant = restaurantList[position]

        // 取得該列中的元件
        val nameTextView = rowView.findViewById<TextView>(R.id.restaurantNameTextView)
        val phoneTextView = rowView.findViewById<TextView>(R.id.restaurantPhoneTextView)

        // 設定資料內容到畫面
        nameTextView.text = restaurant.name
        phoneTextView.text = "電話：${restaurant.phone}"

        return rowView // 回傳顯示的 view
    }
}
