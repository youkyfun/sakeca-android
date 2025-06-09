package com.youkydesign.sakeca.core

import com.youkydesign.sakeca.data.groceries.GroceryEntity
import com.youkydesign.sakeca.domain.groceries.Grocery

object GroceriesDataMapper {
    fun mapEntityToDomain(input: GroceryEntity) = Grocery(
        name = input.name,
        quantity = input.quantity
    )

    fun mapDomainToEntity(input: Grocery) = GroceryEntity(
        name = input.name,
        quantity = input.quantity
    )
}