package com.con19.tripplanner.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.con19.tripplanner.R

class SplashScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_loading_screen, container, false)
        val progress: ImageView = layout.findViewById(R.id.imageView2)
        val rotation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        rotation.setFillAfter(true)
        progress.startAnimation(rotation)
        return layout
    }
}