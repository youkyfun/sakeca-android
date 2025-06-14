package com.youkydesign.sakeca.feature.groceries.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youkydesign.sakeca.core.di.CoreDependenciesProvider
import com.youkydesign.sakeca.feature.groceries.R
import com.youkydesign.sakeca.feature.groceries.di.DaggerGroceriesComponent
import com.youkydesign.sakeca.utils.UiResource
import javax.inject.Inject
import com.youkydesign.sakeca.designsystem.R as brandColors

class GroceriesRootFragment : Fragment() {

    @Inject
    lateinit var factory: GroceriesViewModelFactory

    private val viewModel: GroceriesViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val application = requireActivity().application
        if (application !is CoreDependenciesProvider) {
            throw IllegalStateException("Application must implement CoreDependenciesProvider")
        }
        val coreDependencies = application.provideCoreDependencies()

        DaggerGroceriesComponent.factory()
            .create(
                context = requireActivity(),
                dependencies = coreDependencies
            ).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_groceries, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.shopping_list_compose_view)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val groceries by viewModel.uiState.collectAsStateWithLifecycle()
                Scaffold(
                    modifier = Modifier.background(colorResource(brandColors.color.tw_slate_50)),
                    topBar = {
                        Column(
                            modifier = Modifier
                                .background(colorResource(brandColors.color.tw_slate_50))
                                .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
                        ) {
                            Text("Shopping List", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(brandColors.color.tw_slate_50))
                            .padding(paddingValues),
                    ) {

                        when (groceries) {
                            is UiResource.Error ->
                                Text(groceries.message.toString())

                            is UiResource.Idle -> {}
                            is UiResource.Loading -> Text("Loading")
                            is UiResource.Success -> {
                                LazyColumn(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val groceries = groceries.data
                                    if (groceries.isNullOrEmpty()) {
                                        item {
                                            Text("No groceries")
                                        }
                                    } else {
                                        items(groceries) { grocery ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(colorResource(brandColors.color.tw_slate_200))
                                                    .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
                                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                            ) {
                                                Text(grocery.name.toString())
                                                Text(grocery.quantity.toString())
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

        return view
    }
}

