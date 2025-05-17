package com.youkydesign.core

import com.youkydesign.core.data.local.RecipeEntity
import com.youkydesign.core.data.network.response.RecipeResponse
import com.youkydesign.core.data.network.response.RecipesItem
import com.youkydesign.core.domain.Recipe


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