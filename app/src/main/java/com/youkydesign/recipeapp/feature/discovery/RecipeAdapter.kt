package com.youkydesign.recipeapp.feature.discovery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.youkydesign.core.domain.Recipe
import com.youkydesign.recipeapp.databinding.ItemRowRecipeBinding

class RecipeAdapter(val recipeList: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

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
        val recipe = recipeList[position]

        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(recipeList[holder.bindingAdapterPosition])
        }

    }

    override fun getItemCount(): Int = recipeList.size

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
}