package com.example.foodie_moodie_20

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.example.foodie_moodie_20.roomDatabase.entities.FavouritesEntity
import com.example.foodie_moodie_20.databinding.ActivityDetailsBinding
import com.example.foodie_moodie_20.firebase.firebaseDataModels.FirebaseExtendedIngredient
import com.example.foodie_moodie_20.firebase.firebaseDataModels.FirebaseResult
import com.example.foodie_moodie_20.utils.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodie_moodie_20.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val mFireStore = FirebaseFirestore.getInstance()
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mProgressDialog : Dialog

    private var recipeSaved = false
    private var savedRecipeId = 0

    private lateinit var menuItem: MenuItem

    val user= FirebaseAuth.getInstance().currentUser

    fun getCurrentUserID(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }

    var userANDrecipe :String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions/RecipeBlog")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = com.example.foodie_moodie_20.adapters.PagerAdapter(

            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()

         userANDrecipe = args.result.id.toString() + getCurrentUserID()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {

       if(user==null) {
           mainViewModel.readFavouriteRecipes.observe(this, { favoritesEntity ->
               try {
                   for (savedRecipe in favoritesEntity) {
                       if (savedRecipe.result.id == args.result.id) {
                           changeMenuItemColor(menuItem, R.color.yellow)
                           savedRecipeId = savedRecipe.id
                           recipeSaved = true
                       }
                   }
               } catch (e: Exception) {
                   Log.d("DetailsActivity", e.message.toString())
               }
           })
       }else {
           //lifecycleScope.launch(Dispatchers.IO) {
             mFireStore.collection("savedRecipe").whereEqualTo( "userID", getCurrentUserID())
                           .get().addOnCompleteListener { snapshot->

                       for( s in snapshot.result){
                           if( s.toObject(FirebaseResult::class.java).userANDrecipe == userANDrecipe){
                               changeMenuItemColor(menuItem, R.color.yellow)
                               recipeSaved = true
                               Toast.makeText(this, "Recipes found in favourites ", Toast.LENGTH_SHORT).show()
                               break
                           }
                       }


                   }.addOnFailureListener {
                       Toast.makeText(this, "Favourite Recipes Read Error ", Toast.LENGTH_SHORT).show()
                   }

       }
    }


    private fun saveToFavorites(item: MenuItem) {

        if(user==null) {
            val favoritesEntity =
                FavouritesEntity(
                    0,
                    args.result
                )
            mainViewModel.insertFavouriteRecipe(favoritesEntity)
        }else{

            showProgressDialog()

            val docid = mFireStore.collection("savedRecipe")
                .document().id

        //   var ingredients  = emptyMutableList<FirebaseExtendedIngredient>(args.result.extendedIngredients!!.size)//List<FirebaseExtendedIngredient>(args.result.extendedIngredients!!.size) //   //= args.result.extendedIngredients
            var ingredients = MutableList(args.result.extendedIngredients!!.size){FirebaseExtendedIngredient()}
            for( i in args.result.extendedIngredients!!.indices){
                ingredients[i].amount = args.result.extendedIngredients!![i].amount
                ingredients[i].consistency = args.result.extendedIngredients!![i].consistency
                ingredients[i].id = args.result.extendedIngredients!![i].id
                ingredients[i].image = args.result.extendedIngredients!![i].image
                ingredients[i].name = args.result.extendedIngredients!![i].name
                ingredients[i].original = args.result.extendedIngredients!![i].original
                ingredients[i].unit = args.result.extendedIngredients!![i].unit
            }


            var savedRecipeFirebase = FirebaseResult(
                ingredients,
                args.result.id,
                args.result.image,
                args.result.readyInMinutes,
                args.result.summary,
                args.result.vegan,
                args.result.aggregateLikes,
                args.result.title,
                 getCurrentUserID()!!,
                userANDrecipe,
                args.result.sourceUrl,
                docid
            )



            mFireStore.collection("savedRecipe")
                .document(docid)
                .set(savedRecipeFirebase)//, SetOptions.merge())
                .addOnSuccessListener {
                    hideProgressBar()
                    Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                    //finish()
                }
                .addOnFailureListener{
                    hideProgressBar()
                    Log.e("error","Error while uploading the savedRecipe")
                }

        }
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true
    }



    private fun removeFromFavorites(item: MenuItem) {

        if(user==null) {
            val favoritesEntity =
                FavouritesEntity(
                    savedRecipeId,
                    args.result
                )
            mainViewModel.deleteFavouriteRecipe(favoritesEntity)
        }else{

            val userANDrecipe :String = args.result.id.toString() + getCurrentUserID()

            var delID :String? =null
            lifecycleScope.launch(Dispatchers.IO) {
                var delrecipe = mFireStore.collection("savedRecipe")
                    .whereEqualTo("userANDrecipe", userANDrecipe).get().await()
                Log.i("deleteid", "delreipe is : $delrecipe")

                for (d in delrecipe){
                    Log.i("deleteid", "Doc ID is : ${d.toObject(FirebaseResult::class.java).documentId}")
                    //println(d.toObject(FirebaseResult::class.java).documentId)
                    delID = d.toObject(FirebaseResult::class.java).documentId
                }

                Log.i("deleteid", "Delete ID is : ${delID}")

                if(delID!=null) {
                    mFireStore.collection("savedRecipe").document("${delID}").delete().addOnCompleteListener {
                        Log.i("deleteid", "Document Deleted of Delete ID is : ${delID}")
                    }.addOnFailureListener{
                        Log.i("deleteid", "Document Deletion Error of Delete ID is : ${delID}")
                    }
                }

            }

        }
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(this,R.style.myDialogStyle)
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