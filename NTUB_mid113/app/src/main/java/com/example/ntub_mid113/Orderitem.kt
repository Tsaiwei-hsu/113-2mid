package com.example.pizzaorderapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class OrderType {
    PIZZA,
    SIDE
}

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val details: String,
    val price: Double,
    val quantity: Int,
    val type: OrderType
)

// 檔案: models/Store.kt
package com.example.pizzaorderapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class Store(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val address: String? = null,
    val imagePath: String? = null
)

// 檔案: models/Pizza.kt
package com.example.pizzaorderapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pizzas")
data class Pizza(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val smallPrice: Double,
    val mediumPrice: Double,
    val largePrice: Double
)

// 檔案: models/Side.kt
package com.example.pizzaorderapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sides")
data class Side(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double
)