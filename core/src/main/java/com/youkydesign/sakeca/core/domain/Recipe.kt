package com.youkydesign.sakeca.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val publisher: String,
    val title: String,
    val sourceUrl: String,
    val recipeId: String,
    val imageUrl: String,
    val socialRank: Double,
    val publisherUrl: String,
    val ingredients: List<String> = emptyList<String>(),
    val isFavorite: Boolean = false
) : Parcelable