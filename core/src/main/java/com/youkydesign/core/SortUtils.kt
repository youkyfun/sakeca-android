package com.youkydesign.core

import androidx.sqlite.db.SimpleSQLiteQuery

internal object SortUtils {
    fun getSortedQuery(sortType: RecipeSortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM recipes ")
        when (sortType) {
            RecipeSortType.BY_DATE -> {
                simpleQuery.append("ORDER BY date DESC")
            }

            RecipeSortType.BY_NAME -> {
                simpleQuery.append("ORDER BY title ASC")
            }
        }

        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}