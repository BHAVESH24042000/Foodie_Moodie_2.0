package com.example.foodie_moodie_20

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.foodie_moodie_20.authScreens.CheckUserActivity
import com.example.foodie_moodie_20.databinding.ActivitySplashBinding
import com.example.foodie_moodie_20.firebase.FirebaseUserActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySplashBinding.inflate(layoutInflater)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        setContentView(_binding!!.root)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        }else{
            @Suppress("DEPRECATION")
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        val progressBar = _binding!!.spinKit as ProgressBar
        val wave: Sprite = Wave()
        progressBar.indeterminateDrawable = wave

        Handler(Looper.getMainLooper()).postDelayed({

           // val intent = Intent(this, CheckUserActivity::class.java)
           // startActivity(intent)
           //  finish()


             val user=FirebaseAuth.getInstance().currentUser
            if(user!=null){
                val intent = Intent(this, FirebaseUserActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, CheckUserActivity::class.java)
                startActivity(intent)
                finish()
            }

        },3828)

    }


}