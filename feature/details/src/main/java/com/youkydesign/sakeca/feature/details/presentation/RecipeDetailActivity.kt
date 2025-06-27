package com.youkydesign.sakeca.feature.details.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.youkydesign.sakeca.core.di.CoreDependenciesProvider
import com.youkydesign.sakeca.designsystem.IngredientItem
import com.youkydesign.sakeca.feature.details.di.DaggerRecipeDetailsComponent
import com.youkydesign.sakeca.feature.details.presentation.ui.theme.SakecaandroidTheme
import com.youkydesign.sakeca.utils.UiResource
import javax.inject.Inject

class RecipeDetailActivity : ComponentActivity() {
    private lateinit var navController: NavController

    @Inject
    lateinit var factory: DetailsViewModelFactory

    private val detailRecipeViewModel: DetailRecipeViewModel by viewModels<DetailRecipeViewModel> {
        factory
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val application = this.application
        if (application !is CoreDependenciesProvider) {
            throw IllegalStateException("Application must implement CoreDependenciesProvider")
        }
        val coreDependencies = application.provideCoreDependencies()

        DaggerRecipeDetailsComponent.factory()
            .create(
                context = this,
                dependencies = coreDependencies
            )
            .inject(this)

        setContent {
            SakecaandroidTheme {
                val uiState by detailRecipeViewModel.recipeDetailState.collectAsState(UiResource.Loading())

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Recipe Details") },
                            modifier = TODO(),
                            navigationIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        Icons.Rounded.ArrowBack,
                                        contentDescription = "Navigate up"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    if (uiState.data?.isFavorite != null && uiState.data?.isFavorite == true) {
                                        detailRecipeViewModel.setFavoriteRecipe(
                                            uiState.data,
                                            true
                                        )
                                    }
                                }) {

                                }
                            },
                            expandedHeight = TODO(),
                            windowInsets = TODO(),
                            colors = TODO(),
                            scrollBehavior = TODO(),
                        )
                    }
                ) { paddingValues ->

                    Column(modifier = Modifier.consumeWindowInsets(paddingValues)) {
                        when (uiState) {
                            is UiResource.Error -> {
                                Toast.makeText(
                                    this@RecipeDetailActivity,
                                    uiState.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                            is UiResource.Idle -> {}

                            is UiResource.Loading -> {
                                Toast.makeText(
                                    this@RecipeDetailActivity,
                                    "Loading...",
                                    Toast.LENGTH_SHORT
                                )
                            }

                            is UiResource.Success -> {
                                if (uiState.data == null) {
                                    Toast.makeText(
                                        this@RecipeDetailActivity,
                                        "Sorry, something went wrong! We can't get this recipe right now.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                LazyColumn {
                                    item {
                                        Column {
                                            Text(text = uiState.data?.title ?: "Recipe title")
                                            Text(
                                                text = uiState.data?.publisher ?: "Recipe publisher"
                                            )
                                            Text(
                                                text = (uiState.data?.socialRank
                                                    ?: "0.0").toString()
                                            )
                                        }
                                    }

                                    items(uiState.data?.ingredients ?: emptyList()) { item ->
                                        IngredientItem(
                                            modifier = Modifier,
                                            name = item,
                                            toggleCheck = { state: Boolean ->
                                                if (state == false) {
                                                    detailRecipeViewModel.setIngredientsToSave(item)
                                                } else {
                                                    detailRecipeViewModel.removeIngredientFromList(
                                                        item
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
