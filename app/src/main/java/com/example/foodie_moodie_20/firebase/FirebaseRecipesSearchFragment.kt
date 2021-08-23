package com.example.foodie_moodie_20.firebase

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.api.spoonacularFood.FoodRecipesNetworkResult
import com.example.foodie_moodie_20.databinding.FragmentFirebaseRecipesSearchBinding
import com.example.foodie_moodie_20.databinding.FragmentRecipiesBinding
import com.example.foodie_moodie_20.firebase.ViewModels.FirebaseRecipesViewModel
import com.example.foodie_moodie_20.viewModels.MainViewModel
import com.example.foodie_moodie_20.viewModels.RecipesViewModel
import com.example.foodiemoodie.adapters.RecipesAdapter
import kotlinx.coroutines.launch


class FirebaseRecipesSearchFragment : Fragment(), SearchView.OnQueryTextListener  {

    var backFromBottomSheet: Boolean?= null
    private var binding: FragmentFirebaseRecipesSearchBinding? = null

    private lateinit var mainViewModel: FirebaseRecipesViewModel
   // private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mAdapter : RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()// in here you can do logic when backPress is clicked
            }
        })*/

        mainViewModel = ViewModelProvider(requireActivity()).get(FirebaseRecipesViewModel::class.java)
       // recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        binding= FragmentFirebaseRecipesSearchBinding.inflate(inflater, container, false)

        arguments?.let {
            backFromBottomSheet = it.getBoolean("backFromBottomSheet")
        }

        setHasOptionsMenu(true)
        mAdapter= RecipesAdapter()
        setupRecyclerView()
       // readDatabase()
        requestApiData()

        binding?.resipesfab?.setOnClickListener {
            findNavController().navigate(R.id.action_firebaseRecipesSearchFragment_to_recipesBottomSheet2)
        }

        return binding?.root
    }
    private fun setupRecyclerView() {
        binding?.recyclerview?.adapter = mAdapter
        binding?.recyclerview?.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }




    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!")
        mainViewModel.getRecipes(mainViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is FoodRecipesNetworkResult.Success -> {
                    hideShimmerEffect()
                    // response.data?.let { mAdapter.submitList(it)}
                    mAdapter.submitList(response.data?.results)
                    backFromBottomSheet=false
                    //recipesViewModel.saveMealAndDietType()
                }
                is FoodRecipesNetworkResult.Error -> {
                    hideShimmerEffect()
                   // loadDataFromCache()


                    Toast.makeText(
                        requireContext(),
                        response.message.toString() +
                                " Error ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is FoodRecipesNetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }





    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }
    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(mainViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is FoodRecipesNetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    mAdapter.submitList(response.data?.results)
                }
                is FoodRecipesNetworkResult.Error -> {
                    hideShimmerEffect()

                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is FoodRecipesNetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun showShimmerEffect(){
        binding?.recyclerview?.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding?.recyclerview?.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}