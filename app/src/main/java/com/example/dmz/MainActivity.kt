package com.example.dmz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.dmz.data.repository.MyPageRepositoryImpl
import com.example.dmz.databinding.ActivityMainBinding
import com.example.dmz.viewmodel.MyPageViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val myPageViewModel by viewModels<MyPageViewModel> {
        viewModelFactory { initializer { MyPageViewModel(MyPageRepositoryImpl(this@MainActivity)) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)

        Glide.with(this)
            .load(R.raw.ic_home)
            .into(binding.ivHomeBtn)
    }

    override fun onPause() {
        super.onPause()
        myPageViewModel.saveData()
        binding.ivHomeBtn.setOnClickListener {
            // HomeFragment로 이동
            binding.navView.selectedItemId = R.id.navigation_home
            navController.navigate(R.id.navigation_home)
        }
    }


}