package com.sugarspoon.smartprint

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sugarspoon.smartprint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var smartPrint : SmartPrint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        smartPrint = SmartPrint.Builder(this)
            .setViewForScreenShoot(binding.root)
            .requestPermission()
            .build()
    }

    private fun setListeners() = binding.run {
        printButton.setOnClickListener {
            takeScreenShoot()
        }
    }

    private fun takeScreenShoot() {
        smartPrint.takeAndShareScreenShoot()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions( arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}