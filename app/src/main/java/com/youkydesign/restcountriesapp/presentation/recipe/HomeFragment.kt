package com.youkydesign.restcountriesapp.presentation.recipe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youkydesign.restcountriesapp.R
import com.youkydesign.restcountriesapp.RecipeApplication
import com.youkydesign.restcountriesapp.data.Resource
import com.youkydesign.restcountriesapp.databinding.FragmentHomeBinding
import com.youkydesign.restcountriesapp.domain.Recipe
import com.youkydesign.restcountriesapp.presentation.RecipeAdapter
import com.youkydesign.restcountriesapp.presentation.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvRecipes: RecyclerView

    @Inject
    lateinit var factory: ViewModelFactory

    private val recipeViewModel: RecipeViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as RecipeApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.layoutManager = layoutManager

        rvRecipes = binding.rvRecipes
        rvRecipes.setHasFixedSize(true)

        with(binding) {
            searchBar.inflateMenu(R.menu.app_menu)
            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorite_action -> {
                        Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_detailFragment)
                        true
                    }

                    else -> false
                }
            }

            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                searchView.hide()
                recipeViewModel.searchRecipes(searchBar.text.toString())
                false
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeViewModel.searchResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)
                    if (resource.data.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "No data", Toast.LENGTH_SHORT).show()
                        return@observe
                    }
                    setRecipeList(resource.data)
                }

                is Resource.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Can't load data", Toast.LENGTH_SHORT).show()
                }

                is Resource.Idle -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRecipeList(recipeList: List<Recipe>) {
        val adapter = RecipeAdapter(recipeList)
        binding.rvRecipes.adapter = adapter

        adapter.setOnItemClickCallback(object : RecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                toRecipeDetail(data)
            }

        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun toRecipeDetail(user: Recipe) {
        val bundle = Bundle()
        bundle.putString("rid", user.recipeId)
        view?.findNavController()?.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }
}