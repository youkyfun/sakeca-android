package com.youkydesign.recipeapp.data

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun toString(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }

    @TypeConverter
    fun toList(lineOfString: String): List<String> {
        return lineOfString.split(",")
    }

}