package com.example.foodie_moodie_20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val navController: NavController =findNavController(R.id.navHostfragment)
        val appBarConfiguration: AppBarConfiguration =AppBarConfiguration(
            setOf(
                R.id.recipiesFragment,
                R.id.favouriteRecipiesFragment,
                R.id.restaurantsFragment
            )
        )

        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }
}