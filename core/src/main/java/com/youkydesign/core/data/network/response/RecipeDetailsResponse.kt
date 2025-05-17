package com.youkydesign.core.data.network.response

import com.google.gson.annotations.SerializedName

data class RecipeDetailsResponse(

    @field:SerializedName("recipe")
    val recipeResponse: RecipeResponse
)

data class RecipeResponse(

    @field:SerializedName("social_rank")
    val socialRank: Any,

    @field:SerializedName("recipe_id")
    val recipeId: String,

    @field:SerializedName("publisher_url")
    val publisherUrl: String,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("publisher")
    val publisher: String,

    @field:SerializedName("ingredients")
    val ingredients: List<String>,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("source_url")
    val sourceUrl: String,
)