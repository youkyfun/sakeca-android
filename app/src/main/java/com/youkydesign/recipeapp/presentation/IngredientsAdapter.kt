package com.youkydesign.recipeapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.youkydesign.recipeapp.databinding.ItemRowIngredientsBinding
import com.youkydesign.recipeapp.databinding.ItemRowRecipeBinding
import com.youkydesign.recipeapp.domain.Recipe

class IngredientsAdapter(val recipeList: List<String>) :
    RecyclerView.Adapter<IngredientsAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val binding =
            ItemRowIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecipeViewHolder,
        position: Int
    ) {
        val ingredients = recipeList[position]
        holder.bind(ingredients)
    }

    override fun getItemCount(): Int = recipeList.size

    class RecipeViewHolder(val binding: ItemRowIngredientsBinding) : ViewHolder(binding.root) {
        fun bind(ingredient: String) {
            with(itemView) {
                binding.tvIngredientItem.text = ingredient
            }
        }
    }
}