package com.youkydesign.core

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {
    fun geSortedQuery(sortType: RecipeSortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM recipes where isFavorite = 1 ")
        when (sortType) {
            RecipeSortType.BY_DATE_ASC -> {
                simpleQuery.append("ORDER BY date ASC")
            }

            RecipeSortType.BY_DATE_DESC -> {
                simpleQuery.append("ORDER BY date DESC")
            }

            RecipeSortType.BY_NAME -> {
                simpleQuery.append("ORDER BY title ASC")
            }
        }

        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}