package com.con19.tripplanner.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.con19.tripplanner.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loading_screen)

        val progress : ImageView = findViewById(R.id.imageView2)
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setFillAfter(true);
        progress.startAnimation(rotation);
        lifecycleScope.launch {
            loadAppThenOpen()
        }
    }

    suspend fun loadAppThenOpen() {
        delay(10000L)
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }
}
