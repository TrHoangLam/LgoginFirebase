package com.example.lgoginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lgoginfirebase.databinding.ActivityEndBinding
import com.example.lgoginfirebase.databinding.ActivityGetStartedBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EndActivity : AppCompatActivity() {
    private var binding:ActivityEndBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        binding = ActivityEndBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.tvend?.setOnClickListener {
            startActivities(arrayOf(Intent(this,EndActivity::class.java)))
            finish()
        }

    }
}