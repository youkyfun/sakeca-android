package com.youkydesign.favorite.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.youkydesign.core.domain.Recipe
import com.youkydesign.recipeapp.databinding.ItemRowRecipeBinding

internal class FavoriteAdapter :
    PagingDataAdapter<Recipe, FavoriteAdapter.RecipeViewHolder>(DIFF_CALBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val binding =
            ItemRowRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecipeViewHolder,
        position: Int
    ) {
        val recipe = getItem(position) as Recipe

        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(recipe)
        }

    }

    class RecipeViewHolder(val binding: ItemRowRecipeBinding) : ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            with(itemView) {
                Glide.with(binding.imgRecipeImage).load(recipe.imageUrl)
                    .into(binding.imgRecipeImage)
                binding.tvRecipeTitle.text = recipe.title
                binding.tvRecipePublisher.text = recipe.publisher
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Recipe)
    }

    companion object {
        private val DIFF_CALBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem.recipeId == newItem.recipeId
            }

            override fun areContentsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}