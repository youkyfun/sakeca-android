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
    )

    fun mapDomainToEntity(input: Recipe) = RecipeEntity(
        publisher = input.publisher,
        title = input.title,
        sourceUrl = input.sourceUrl,
        recipeId = input.recipeId,
        imageUrl = input.imageUrl,
        socialRank = input.socialRank,
        publisherUrl = input.publisherUrl,
    )

    fun mapResponsesToEntities(input: List<RecipesItem>) = input.map {
        RecipeEntity(
            publisher = it.publisher,
            title = it.title,
            sourceUrl = it.sourceUrl,
            recipeId = it.recipeId,
            imageUrl = it.imageUrl,
            socialRank = (it.socialRank).toDouble(),
            publisherUrl = it.publisherUrl,
        )
    }

    fun mapResponseToDomain(input: RecipesItem) = Recipe(
        publisher = input.publisher,
        title = input.title,
        sourceUrl = input.sourceUrl,
        recipeId = input.recipeId,
        imageUrl = input.imageUrl,
        socialRank = (input.socialRank).toDouble(),
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
    )

}