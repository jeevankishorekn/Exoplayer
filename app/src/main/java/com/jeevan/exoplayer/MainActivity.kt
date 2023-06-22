package com.jeevan.exoplayer

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeevan.exoplayer.adapters.AllMusicAdapter
import com.jeevan.exoplayer.databinding.ActivityMainBinding
import com.jeevan.exoplayer.model.MusicItem

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        val permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it.entries.forEach { permission ->
                    when (permission.key) {
                        READ_MEDIA_AUDIO -> if (!permission.value) Log.d(
                            TAG,
                            "onCreate: Permission denied READ_MEDIA_AUDIO"
                        )
                        READ_MEDIA_VIDEO -> if (!permission.value) Log.d(
                            TAG,
                            "onCreate: Permission denied READ_MEDIA_VIDEO"
                        )
                    }
                }
            }
        permissionRequest.launch(arrayOf(READ_MEDIA_AUDIO, READ_MEDIA_VIDEO, READ_EXTERNAL_STORAGE))

    }

}