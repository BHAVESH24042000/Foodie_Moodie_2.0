package com.example.foodie_moodie_20.firebase

import android.app.Dialog
import android.graphics.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.api.spoonacularFood.FoodRecipesRemoteDataSource
import com.example.foodie_moodie_20.databinding.FragmentFirebaseRecipesFavouriteBinding
import com.example.foodie_moodie_20.firebase.ViewModels.FirebaseRecipesViewModel
import com.example.foodie_moodie_20.firebase.adapters.FirebaseFavouriteRecipesAdapter2
import com.example.foodie_moodie_20.firebase.firebaseDataModels.FirebaseResult
import com.example.shopperista.utils.SwipeToDeleteCallback
import com.example.shopperista.utils.SwipeToEditCallback
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_firebase_recipes_favourite.*
import kotlinx.coroutines.launch


class FirebaseRecipesFavouriteFragment : Fragment() {

    private lateinit var mainViewModel: FirebaseRecipesViewModel
    private var mAdapter: FirebaseFavouriteRecipesAdapter2? = null
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mProgressDialog : Dialog
    private var _binding: FragmentFirebaseRecipesFavouriteBinding? = null
    private val binding get() = _binding!!
    var userListener: ListenerRegistration? = null
    private val database by lazy {
        FirebaseFirestore.getInstance().collection("savedRecipe")
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser!!.uid
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentFirebaseRecipesFavouriteBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity()).get(FirebaseRecipesViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onResume() {
        super.onResume()
        getListFromFireStore()
    }

    private fun getListFromFireStore(){
        showProgressDialog()
        //FireStoreClass().getMemoriesList(this)
        FirebaseFirestore.getInstance().collection("savedRecipe").whereEqualTo( "userID", getCurrentUserID())
            .get()
            .addOnCompleteListener { list->
                val memoryList : ArrayList<FirebaseResult> = ArrayList()
                for(i in list.result){
                    val memory =i.toObject(FirebaseResult::class.java)!!

                    //val memoryHashmap = HashMap<String,Any>()
                    //memoryHashmap["memoryID"]=i.id
                   // mFireStore.collection("memories")
                    //    .document(i.id)
                      //  .update(memoryHashmap)

                    memoryList.add(memory)
                }

                listReceivedSuccessfully(memoryList)

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error in Fetching Favourites Recipes", Toast.LENGTH_SHORT).show()
                hideProgressBar()
            }
    }

    fun listReceivedSuccessfully(memoryList: ArrayList<FirebaseResult>) {
        hideProgressBar()
        if(memoryList.size>0){

            //mBinding!!.rvPersonal.layoutManager= LinearLayoutManager(requireContext())
            //val rvAdapter= PersonalMemoryAdapter(requireContext(),memoryList)
            //mBinding!!.rvPersonal.adapter=rvAdapter

            mAdapter = FirebaseFavouriteRecipesAdapter2(requireContext(), memoryList)
            favoriteRecipesRecyclerView2.adapter = mAdapter
            favoriteRecipesRecyclerView2.layoutManager = LinearLayoutManager(requireContext())


            val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val adapter = _binding!!.favoriteRecipesRecyclerView2.adapter as FirebaseFavouriteRecipesAdapter2
                    showProgressDialog()
                    adapter.notifyDeleteItem(this@FirebaseRecipesFavouriteFragment,viewHolder.adapterPosition)
                    hideProgressBar()
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView( _binding!!.favoriteRecipesRecyclerView2)

            val editSwipeHandler = object : SwipeToEditCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                   // val adapter = mBinding!!.rvPersonal.adapter as PersonalMemoryAdapter
                    //adapter.notifyEditItem(this@FirebasePersonalFragment, viewHolder.adapterPosition)
                    lifecycleScope.launch() {
                        val response = FoodRecipesRemoteDataSource().searchRecipes(mainViewModel.applySearchQuery(
                            //mAdapter!!.singleRecipe.title!!
                         memoryList[viewHolder.adapterPosition].title!!
                        ))

                        val action = FirebaseRecipesFavouriteFragmentDirections.actionFirebaseRecipesFavouriteFragmentToDetailsActivity(
                            response.body()?.results?.get(0)!!
                        )
                        view?.findNavController()?.navigate(action)

                    }


                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView( _binding!!.favoriteRecipesRecyclerView2)

        }else{
            //mBinding!!.rvPersonal.visibility=View.GONE
            //mBinding!!.tvPersonal.visibility=View.VISIBLE
        }

    }

    fun deleteSuccessful() {
        hideProgressBar()
        Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT).show()
        getListFromFireStore()
    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(requireContext(),R.style.myDialogStyle)
        mProgressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressBar(){
        mProgressDialog.dismiss()
    }

}