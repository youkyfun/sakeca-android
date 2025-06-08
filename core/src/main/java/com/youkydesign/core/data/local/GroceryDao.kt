package com.youkydesign.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GroceryDao {
    @Query("SELECT * FROM grocery")
    fun getAll(): List<GroceryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(grocery: GroceryEntity)

    @Update
    fun update(grocery: GroceryEntity)

    @Delete
    fun delete(grocery: GroceryEntity)

    @Query("DELETE FROM grocery")
    fun deleteAll()

}