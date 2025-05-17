package com.youkydesign.core.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeListResponse(

    @field:SerializedName("recipes")
    val recipes: List<RecipesItem>,

    @field:SerializedName("count")
    val count: Int
) : Parcelable

@Parcelize
data class RecipesItem(

    @field:SerializedName("social_rank")
    val socialRank: Double,

    @field:SerializedName("recipe_id")
    val recipeId: String,

    @field:SerializedName("publisher_url")
    val publisherUrl: String,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("publisher")
    val publisher: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("source_url")
    val sourceUrl: String,

    ) : Parcelable
