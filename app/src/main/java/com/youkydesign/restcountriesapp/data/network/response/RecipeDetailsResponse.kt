package com.youkydesign.restcountriesapp.data.network.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RecipeDetailsResponse(

	@field:SerializedName("recipeResponse")
	val recipeResponse: RecipeResponse
) : Parcelable

@Parcelize
data class RecipeResponse(

	@field:SerializedName("social_rank")
	val socialRank: Int,

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
	val sourceUrl: String
) : Parcelable
