package com.youkydesign.sakeca.favorite.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.youkydesign.sakeca.favorite.databinding.ItemRowIngredientsBinding

internal class IngredientsAdapter(val ingredients: List<String>) :
    RecyclerView.Adapter<IngredientsAdapter.GroceriesViewHolder>() {

    private lateinit var onItemClick: OnItemClickCallback

    fun setOnItemClickCallback(onItemClick: OnItemClickCallback) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroceriesViewHolder {
        val binding =
            ItemRowIngredientsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return GroceriesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GroceriesViewHolder,
        position: Int
    ) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
        holder.itemView.setOnClickListener {
            onItemClick.onItemClicked(ingredients[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = ingredients.size

    class GroceriesViewHolder(val binding: ItemRowIngredientsBinding) :
        ViewHolder(binding.root) {
        fun bind(ingredient: String) {
            with(itemView) {
                binding.tvIngredientItem.text = ingredient
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}