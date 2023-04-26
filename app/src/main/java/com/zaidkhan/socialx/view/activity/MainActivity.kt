package com.zaidkhan.socialx.view.activity

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zaidkhan.socialx.R
import com.zaidkhan.socialx.databinding.ActivityMainBinding
import com.zaidkhan.socialx.view.fragment.LoginFragment

class MainActivity : AppCompatActivity(), LoginFragment.MainActivityCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Change the size of the 7th character, which is at index 6
        val spannableTitle = SpannableString(binding.toolbar.title)
        spannableTitle.setSpan(RelativeSizeSpan(1.5f), 6, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        supportActionBar?.title = spannableTitle

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController

        //Setting Default drawable on Login button
        binding.btnLogin.background = resources.getDrawable(R.drawable.bg_nav_bar_item)
        binding.btnLogin.setTextColor(resources.getColor(R.color.white))

        binding.btnLogin.setOnClickListener {
            binding.btnSignup.setBackgroundResource(0)
            binding.btnSignup.setTextColor(resources.getColor(R.color.gray_500))
            binding.btnLogin.setTextColor(resources.getColor(R.color.white))
            binding.btnLogin.background = resources.getDrawable(R.drawable.bg_nav_bar_item)
            navigateToDestination(R.id.loginFragment)
        }

        binding.btnSignup.setOnClickListener {
            binding.btnLogin.setBackgroundResource(0)
            binding.btnLogin.setTextColor(resources.getColor(R.color.gray_500))
            binding.btnSignup.setTextColor(resources.getColor(R.color.white))
            binding.btnSignup.background = resources.getDrawable(R.drawable.bg_nav_bar_item)
            navigateToDestination(R.id.signupFragment)
        }
    }

    private fun navigateToDestination(destinationId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController
        navController.navigate(destinationId)
    }

    override fun onBackPressed() {
        val currentFragmentId = navController.currentDestination?.id
        if (currentFragmentId != R.id.loginFragment) {
            navigateToDestination(R.id.loginFragment)
            binding.btnSignup.setBackgroundResource(0)
            binding.btnSignup.setTextColor(resources.getColor(R.color.gray_500))
            binding.btnLogin.setTextColor(resources.getColor(R.color.white))
            binding.btnLogin.background = resources.getDrawable(R.drawable.bg_nav_bar_item)
        } else {
            finish()
        }
    }

    override fun signupToLogin(){
        binding.btnSignup.setBackgroundResource(0)
        binding.btnSignup.setTextColor(resources.getColor(R.color.gray_500))
        binding.btnLogin.setTextColor(resources.getColor(R.color.white))
        binding.btnLogin.background = resources.getDrawable(R.drawable.bg_nav_bar_item)
    }

    override fun loginToSignup() {
        binding.btnLogin.setBackgroundResource(0)
        binding.btnSignup.setTextColor(resources.getColor(R.color.white))
        binding.btnLogin.setTextColor(resources.getColor(R.color.gray_500))
        binding.btnSignup.background = resources.getDrawable(R.drawable.bg_nav_bar_item)
    }
}
