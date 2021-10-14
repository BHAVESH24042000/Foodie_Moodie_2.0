package com.example.foodie_moodie_20.uiScreens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie_moodie_20.adapters.IngredientsAdapter
import com.example.foodie_moodie_20.databinding.FragmentIngredientsBinding
import com.example.foodie_moodie_20.utils.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodiepoodie.dataModels.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    private var binding: FragmentIngredientsBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView()
        myBundle?.extendedIngredients?.let { mAdapter.submitList(it) }

        return binding!!.root
    }

    private fun setupRecyclerView() {
        binding?.ingredientsRecyclerview?.adapter= mAdapter
        binding?.ingredientsRecyclerview?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}