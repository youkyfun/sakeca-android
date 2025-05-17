package com.youkydesign.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youkydesign.core.domain.Recipe
import com.youkydesign.core.domain.UiResource
import com.youkydesign.favorite.databinding.ActivityFavoriteBinding
import com.youkydesign.favorite.di.DaggerFavoriteComponent
import com.youkydesign.recipeapp.RecipeApplication
import com.youkydesign.recipeapp.feature.discovery.RecipeAdapter
import com.youkydesign.recipeapp.feature.discovery.ui.DetailFragment
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var rvFavoriteRecipes: RecyclerView

    @Inject
    lateinit var factory: FavoriteViewModelFactory

    private val viewModel: FavoriteRecipeViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val dependencies = (application as RecipeApplication).appComponent
        DaggerFavoriteComponent.factory().create(this, dependencies).inject(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteRecipes.layoutManager = layoutManager
        rvFavoriteRecipes = binding.rvFavoriteRecipes
        rvFavoriteRecipes.setHasFixedSize(true)

        viewModel.favoriteRecipes.observe(this) { resource ->
            when (resource) {
                is UiResource.Loading -> {
                    showLoading(true)
                }

                is UiResource.Error -> {
                    showLoading(false)
                    Toast.makeText(this@FavoriteActivity, "Can't load data", Toast.LENGTH_SHORT)
                        .show()
                }

                is UiResource.Idle -> {}
                is UiResource.Success -> {
                    showLoading(false)
                    if (resource.data.isNullOrEmpty()) {
                        Toast.makeText(this@FavoriteActivity, "No data", Toast.LENGTH_SHORT).show()
                        return@observe
                    }
                    if (resource.data != null) {
                        setRecipeList(resource.data!!)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setRecipeList(recipeList: List<Recipe>) {
        val adapter = RecipeAdapter(recipeList)
        binding.rvFavoriteRecipes.adapter = adapter

        adapter.setOnItemClickCallback(object : RecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }

        })
    }

    private fun toRecipeDetail(user: Recipe) {
        val intent = Intent(this, DetailFragment::class.java)
        intent.putExtra("rid", user.recipeId)
        startActivity(intent)
    }
}