package com.youkydesign.restcountriesapp.data

import com.youkydesign.restcountriesapp.data.local.RecipeEntity
import com.youkydesign.restcountriesapp.data.network.response.RecipeResponse
import com.youkydesign.restcountriesapp.data.network.response.RecipesItem
import com.youkydesign.restcountriesapp.domain.Recipe

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

    fun mapResponseToEntity(input: RecipeResponse) = RecipeEntity(
        publisher = input.publisher,
        title = input.title,
        sourceUrl = input.sourceUrl,
        recipeId = input.recipeId,
        imageUrl = input.imageUrl,
        socialRank = (input.socialRank).toDouble(),
        publisherUrl = input.publisherUrl,
    )

}