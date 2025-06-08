package com.youkydesign.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery")
data class GroceryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int
)
