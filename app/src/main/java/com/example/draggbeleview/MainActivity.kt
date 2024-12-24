package com.example.draggbeleview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.draggbeleview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.tvGreeting.text = getString(R.string.greeting)
        Glide.with(this).asBitmap()
            .load(getString(R.string.source_url))
            .into(binding.ivSource)
        Glide.with(this).asBitmap()
            .load(getString(R.string.target_url))
            .into(binding.ivTarget)
    }

}