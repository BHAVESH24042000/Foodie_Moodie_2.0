package com.example.foodie_moodie_20.firebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.databinding.ActivityFirebaseMemoryDetailsBinding
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Memory
import com.example.foodie_moodie_20.utils.Constants
import com.example.foodie_moodie_20.utils.GlideLoader
import java.util.*

class FirebaseMemoryDetailsActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityFirebaseMemoryDetailsBinding
    private  var mModel : Memory? = null
    private var city : String = " "
    private var isFavorite:String=" "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityFirebaseMemoryDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if(intent.hasExtra(Constants.MEMORY_DETAILS)) {
            mModel = intent.getParcelableExtra(Constants.MEMORY_DETAILS)!!
            GlideLoader(this).loadPicture(mModel!!.imageURL, mBinding.ivMemoryImage)
            mBinding.tvTitle.text = mModel!!.title
            mBinding.tvDescription.text = mModel!!.description
            mBinding.tvAddress.text = mModel!!.location
            mBinding.tvDate.text = mModel!!.date
           // mBinding.cityName.text = mModel!!.cityName.uppercase(Locale.getDefault())
            city = mModel!!.cityName
           // getWeatherData()


        }


        mBinding.tvLocation.setOnClickListener{
            val strUri = "http://maps.google.com/maps?q= ${mModel!!.location}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            startActivity(intent)
        }


        setUpToolbar()
    }



    private fun setUpToolbar() {
        setSupportActionBar(mBinding.memoryDetailToolbar)
        val actionBar=supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar.title="Memory Details"
        mBinding.memoryDetailToolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}