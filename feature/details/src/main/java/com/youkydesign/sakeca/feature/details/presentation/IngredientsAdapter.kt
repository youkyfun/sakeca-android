package com.youkydesign.sakeca.feature.details.presentation

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.youkydesign.sakeca.feature.details.databinding.ItemRowIngredientsBinding

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
            ItemRowIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceriesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GroceriesViewHolder,
        position: Int
    ) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
        holder.itemView.setOnClickListener {
            val isChecked = !holder.binding.ingredientsCheckbox.isChecked
            holder.binding.ingredientsCheckbox.isChecked = isChecked
            if (isChecked) {
                holder.binding.tvIngredientItem.setTypeface(null, Typeface.BOLD)
            } else {
                holder.binding.tvIngredientItem.setTypeface(null, Typeface.NORMAL)
            }
            onItemClick.onItemClicked(ingredients[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = ingredients.size

    class GroceriesViewHolder(val binding: ItemRowIngredientsBinding) : ViewHolder(binding.root) {
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