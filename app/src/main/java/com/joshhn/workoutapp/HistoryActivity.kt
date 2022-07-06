package com.joshhn.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joshhn.workoutapp.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "History"
        }

        //Add back Button to Toolbar
        binding?.toolbarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}