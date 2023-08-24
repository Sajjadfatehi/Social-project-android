package com.example.nattramn.features.home.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.nattramn.R
import com.example.nattramn.databinding.FragmentSplashBinding
import com.example.nattramn.features.user.data.AuthLocalDataSource

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val splashTimeOut = 2000.toLong()
    private val localDataSource = AuthLocalDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideSystemUI()

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )

        Handler().postDelayed({
            checkTokenAndNavigate()
        }, splashTimeOut)

        return binding.root
    }

    private fun checkTokenAndNavigate() {
        if (localDataSource.getToken().isNullOrEmpty()) {
            Navigation.findNavController(requireView())
                .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        } else {
            Navigation.findNavController(requireView())
                .navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemUI() {
        activity?.window?.decorView?.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}