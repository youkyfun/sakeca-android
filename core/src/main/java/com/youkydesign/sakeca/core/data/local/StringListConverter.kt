package com.youkydesign.sakeca.core.data.local

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun toString(listOfString: List<String>): String {
        return listOfString.joinToString(",")
    }

    @TypeConverter
    fun toList(lineOfString: String): List<String> {
        if (lineOfString.isEmpty()) return emptyList()
        return lineOfString.split(",")
    }

}