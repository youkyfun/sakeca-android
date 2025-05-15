package com.youkydesign.recipeapp.data

import com.youkydesign.recipeapp.data.local.RecipeEntity
import com.youkydesign.recipeapp.data.network.response.RecipeResponse
import com.youkydesign.recipeapp.data.network.response.RecipesItem
import com.youkydesign.recipeapp.domain.Recipe

object DataMapper {
    fun mapEntityToDomain(input: RecipeEntity) = Recipe(
        publisher = input.publisher,
        title = input.title,
        sourceUrl = input.sourceUrl,
        recipeId = input.recipeId,
        imageUrl = input.imageUrl,
        socialRank = input.socialRank,
        publisherUrl = input.publisherUrl,
        ingredients = input.ingredients
    )

    fun mapListItemResponseToDomain(input: RecipesItem) = Recipe(
        publisher = input.publisher,
        title = input.title,
        sourceUrl = input.sourceUrl,
        recipeId = input.recipeId,
        imageUrl = input.imageUrl,
        socialRank = input.socialRank,
        publisherUrl = input.publisherUrl,
    )

    fun mapResponseToEntity(input: RecipeResponse) = RecipeEntity(
        publisher = input.publisher,
        title = input.title,
        sourceUrl = input.sourceUrl,
        recipeId = input.recipeId,
        imageUrl = input.imageUrl,
        socialRank = input.socialRank as Double,
        publisherUrl = input.publisherUrl,
        ingredients = input.ingredients
    )

}