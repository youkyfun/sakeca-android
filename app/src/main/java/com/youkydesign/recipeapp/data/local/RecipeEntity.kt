package com.youkydesign.recipeapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    @ColumnInfo(name = "recipeId")
    val recipeId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "publisher")
    val publisher: String,
    
    @ColumnInfo(name = "publisherUrl")
    val publisherUrl: String,

    @ColumnInfo(name = "sourceUrl")
    val sourceUrl: String,

    @ColumnInfo(name = "socialRank")
    val socialRank: Double,

    @ColumnInfo(name = "ingredients")
    val ingredients: List<String>
)
