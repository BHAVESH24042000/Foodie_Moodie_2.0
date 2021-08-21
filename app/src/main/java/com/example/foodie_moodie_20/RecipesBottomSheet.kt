package com.example.foodie_moodie_20

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodie_moodie_20.databinding.FragmentRecipesBottomSheetBinding
import com.example.foodie_moodie_20.firebase.ViewModels.FirebaseRecipesViewModel
import com.example.foodie_moodie_20.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodie_moodie_20.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodie_moodie_20.viewModels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class  RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var firebaseRecipesViewModel: FirebaseRecipesViewModel
    private var binding: FragmentRecipesBottomSheetBinding? =null
    val user= FirebaseAuth.getInstance().currentUser

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        firebaseRecipesViewModel = ViewModelProvider(requireActivity()).get(FirebaseRecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

        if(user==null) {
            recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(value.selectedMealTypeId, binding!!.mealTypeChipGroup)
                updateChip(value.selectedDietTypeId, binding!!.dietTypeChipGroup)
            })
        }
        else{

            firebaseRecipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner, { value ->
                mealTypeChip = value.selectedMealType
                dietTypeChip = value.selectedDietType
                updateChip(value.selectedMealTypeId, binding!!.mealTypeChipGroup)
                updateChip(value.selectedDietTypeId, binding!!.dietTypeChipGroup)
            })

        }

        binding!!.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding!!.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }




        binding!!.applyBtn.isVisible=true

        if(user==null) {
            binding!!.applyBtn.setOnClickListener {
                recipesViewModel.saveMealAndDietType(
                    mealTypeChip,
                    mealTypeChipId,
                    dietTypeChip,
                    dietTypeChipId
                )


                findNavController().navigate(
                    R.id.action_recipesBottomSheet_to_recipiesFragment,
                    bundleOf("backFromBottomSheet" to true)
                )
            }
        }else{

            binding!!.applyBtn.setOnClickListener {
                firebaseRecipesViewModel.saveMealAndDietType(
                    mealTypeChip,
                    mealTypeChipId,
                    dietTypeChip,
                    dietTypeChipId
                )


                findNavController().navigate(
                    R.id.action_recipesBottomSheet2_to_firebaseRecipesSearchFragment,
                    bundleOf("backFromBottomSheet" to true)
                )
            }


        }

        return binding!!.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}