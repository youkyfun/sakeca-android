package com.youkydesign.sakeca.feature.groceries.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youkydesign.sakeca.core.di.CoreDependenciesProvider
import com.youkydesign.sakeca.feature.groceries.R
import com.youkydesign.sakeca.feature.groceries.di.DaggerGroceriesComponent
import com.youkydesign.sakeca.utils.UiResource
import javax.inject.Inject

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
                Scaffold { paddingValues ->
                    Column(
                        modifier = Modifier.padding(paddingValues),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (groceries) {
                            is UiResource.Error ->
                                Text(groceries.message.toString())

                            is UiResource.Idle -> {}
                            is UiResource.Loading -> Text("Loading")
                            is UiResource.Success -> {
                                LazyColumn {
                                    items(groceries.data?.size ?: 0) { index ->
                                        Row {
                                            Text(groceries.data?.get(index)?.name.toString())
                                            Text(groceries.data?.get(index)?.quantity.toString())
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

