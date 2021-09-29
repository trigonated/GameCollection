package com.trigonated.gamecollection.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trigonated.gamecollection.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Uncomment to start with a fresh database on every run
//        deleteDatabase(AppDatabase.DATABASE_NAME)

        setContentView(R.layout.activity_main)
    }
}